package capstone.jejuTourrecommend.wishList.infrastructure.repository;

import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.spot.domain.mainSpot.Location;
import capstone.jejuTourrecommend.wishList.domain.dto.PictureUrlDto;
import capstone.jejuTourrecommend.wishList.domain.dto.QRouteSpotListDto;
import capstone.jejuTourrecommend.wishList.domain.dto.RouteSpotListDto;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.RouteForm;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static capstone.jejuTourrecommend.spot.domain.QScore.score;
import static capstone.jejuTourrecommend.spot.domain.QSpot.spot;
import static capstone.jejuTourrecommend.spot.domain.detailSpot.QPicture.picture;
import static capstone.jejuTourrecommend.wishList.domain.QFavoriteSpot.favoriteSpot;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
@Slf4j
@Transactional
public class FavoriteSpotQuerydslRepositoryImpl implements FavoriteSpotQuerydslRepository{

	private final JPAQueryFactory queryFactory;

	public FavoriteSpotQuerydslRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	public List<Long> getBooleanFavoriteSpot(Long memberId, List<Long> spotIdList) {

		List<Long> favoriteSpotIdList = queryFactory
			.select(
				favoriteSpot.spot.id
			)
			.from(favoriteSpot)
			.where(favoriteSpot.favorite.member.id.eq(memberId), favoriteSpot.spot.id.in(spotIdList))
			.fetch();

		return favoriteSpotIdList;

	}

	/**
	 * 사용자가 선호하는 우선순위 카테고리에 따라, 최단 경로 주변 관광지 추천해주기
	 *
	 * @param favoriteId
	 * @param routeForm
	 * @return
	 */
	public List recommendSpotList(Long favoriteId, RouteForm routeForm) {

		//favoriteId 에 있는 spotid 리스트를 가지고 옴
		List<Long> spotIdList = getSpotIdList(favoriteId, routeForm);

		// 가져온 spotId 리스트로 점수가 제일 높은 카테고리 구하고 정렬 기준 정하기
		OrderSpecifier<Double> orderSpecifier = getDoubleOrderSpecifier(spotIdList);

		List<List<RouteSpotListDto>> list = getRecommandedSpotListByLocation(spotIdList, orderSpecifier);

		return list;
	}

	/**
	 * 지역별 추천된 관광지 담기
	 *
	 * @param spotIdList
	 * @param orderSpecifier
	 * @return
	 */
	private List<List<RouteSpotListDto>> getRecommandedSpotListByLocation(List<Long> spotIdList,OrderSpecifier<Double> orderSpecifier) {

		List<List<RouteSpotListDto>> list = new ArrayList<>();

		//위시리스트에 있는 관광지들이 속한 location 리스트 가져오기
		List<Location> locationList = queryFactory
			.select(spot.location).distinct()
			.from(spot)
			.where(spot.id.in(spotIdList))
			.fetch();

		log.info("location = {}", locationList);

		//location 리스트 별로 top 10 관광지 정보가져오기
		for (Location location : locationList) {

			List<RouteSpotListDto> spotListDtos = queryFactory
				.select(new QRouteSpotListDto(
						spot.id,
						spot.name,
						spot.address,
						spot.description,
						spot.location
					)
				)
				.from(spot)
				.where(locationEq(location))
				.orderBy(orderSpecifier)
				.offset(0)
				.limit(10)
				.fetch();

			postSpotPictureUrlsToDto(spotListDtos);

			list.add(spotListDtos);

		}
		return list;
	}

	private void postSpotPictureUrlsToDto(List<RouteSpotListDto> spotListDtos) {

		//해당 지역에 있는 top 10 지역들 가져오기
		List<Long> spotIdList = spotListDtos.stream().map(s -> s.getSpotId()).collect(Collectors.toList());

		List<PictureUrlDto> pictureUrlDtos = queryFactory
			.select(Projections.constructor(PictureUrlDto.class,
					picture.spot.id,
					picture.url.max()
				)
			)
			.from(picture)
			.innerJoin(picture.spot, spot)
			.where(picture.spot.id.in(spotIdList))
			.groupBy(picture.spot.id)
			.fetch();

		Map<Long, List<PictureUrlDto>> collect = pictureUrlDtos.stream()
			.collect(Collectors.groupingBy(p -> p.getSpotId()));

		if (collect.isEmpty()) {//사진이 없는 경우
			return;
		}

		spotListDtos.forEach(sl -> sl.setUrl(collect.get(sl.getSpotId()).get(0).getPictureUrl()));

	}

	/**
	 * 특정 위시리시트에 있는 관광지 리스트 조회
	 *
	 * @param favoriteId
	 * @param routeForm
	 * @return
	 */
	private List<Long> getSpotIdList(Long favoriteId, RouteForm routeForm) {
		List<Long> spotIdList = queryFactory
			.select(favoriteSpot.spot.id)
			.from(favoriteSpot)
			.innerJoin(favoriteSpot.spot, spot)
			.where(favoriteIdEq(favoriteId), spot.id.in(routeForm.getSpotIdList()))
			.fetch();

		if (isEmpty(spotIdList)) {
			log.info("spotIdList = {}", spotIdList);
			throw new UserException("모든 spotId가 위시리스트에 있는 spotId가 아닙니다");
		}
		return spotIdList;
	}

	/**
	 * 특정 위시리스트에 있는 각 관광지들의 카테고리별 합산 점수 조회 및 정렬 기준 정하기
	 *
	 * @param spotIdList
	 * @return
	 */
	private OrderSpecifier<Double> getDoubleOrderSpecifier(List<Long> spotIdList) {

		List<Tuple> tupleList = queryFactory
			.select(
				spot.score.viewScore.sum(),
				spot.score.priceScore.sum(),
				spot.score.facilityScore.sum(),
				spot.score.surroundScore.sum()
			)
			.from(spot)
			.innerJoin(spot.score, score)
			.where(spot.id.in(spotIdList))
			.fetch();

		//최대값을 가진 카테고리 구하기
		Tuple tuple = tupleList.get(0);

		Double[] score = new Double[4];
		score[0] = tuple.get(spot.score.viewScore.sum());
		log.info("score[0] = {}", score[0]);
		score[1] = tuple.get(spot.score.priceScore.sum());
		score[2] = tuple.get(spot.score.facilityScore.sum());
		score[3] = tuple.get(spot.score.surroundScore.sum());

		Double max = score[0];
		int j = 0;

		for (int i = 0; i < 4; i++) {
			if (max < score[i]) {
				j = i;
				max = score[i];
			}
		}

		//최대값을 가진 인덱스를 찾아서 해당 카테고리벼로 내림 차순을 할 것임
		OrderSpecifier<Double> orderSpecifier;
		if (j == 0)
			orderSpecifier = spot.score.viewScore.desc();
		else if (j == 1)
			orderSpecifier = spot.score.priceScore.desc();
		else if (j == 2)
			orderSpecifier = spot.score.facilityScore.desc();
		else
			orderSpecifier = spot.score.surroundScore.desc();
		return orderSpecifier;
	}

	private BooleanExpression favoriteIdEq(Long favoriteId) {
		return isEmpty(favoriteId) ? null : favoriteSpot.favorite.id.eq(favoriteId);
	}

	private BooleanExpression locationEq(Location location) {
		return location != null ? spot.location.eq(location) : null;
	}

}






