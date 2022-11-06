package capstone.jejuTourrecommend.wishList.infrastructure.repository;

import static capstone.jejuTourrecommend.authentication.domain.QMember.*;
import static capstone.jejuTourrecommend.spot.domain.QScore.*;
import static capstone.jejuTourrecommend.spot.domain.QSpot.*;
import static capstone.jejuTourrecommend.spot.domain.detailSpot.QPicture.*;
import static capstone.jejuTourrecommend.wishList.domain.QFavorite.*;
import static capstone.jejuTourrecommend.wishList.domain.QFavoriteSpot.*;
import static org.springframework.util.ObjectUtils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.mainSpot.Location;
import capstone.jejuTourrecommend.wishList.domain.FavoriteSpot;
import capstone.jejuTourrecommend.wishList.domain.QFavoriteSpot;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import capstone.jejuTourrecommend.wishList.domain.dto.PictureUrlDto;
import capstone.jejuTourrecommend.wishList.domain.dto.QRouteSpotListDto;
import capstone.jejuTourrecommend.wishList.domain.dto.RouteSpotListDto;
import capstone.jejuTourrecommend.wishList.domain.dto.SpotListDtoByFavoriteSpot;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.RouteForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

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
	 * 사용자 위시리스트 페이지 보여주기
	 *
	 * @param memberId
	 * @param pageable
	 * @return
	 */
	public Page<FavoriteListDto> getFavoriteList(Long memberId, Pageable pageable) {

		List<FavoriteListDto> favoriteListDtos = queryFactory
			.select(
				Projections.constructor(
					FavoriteListDto.class,
					favorite.id,
					favorite.name
				)
			)
			.from(favorite)
			.where(favorite.member.id.eq(memberId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		postPictureUrlOnFavoriteList(favoriteListDtos);

		JPAQuery<Long> countQuery = queryFactory
			.select(favorite.count())
			.from(favorite)
			.innerJoin(favorite.member, member)
			.where(favorite.member.id.eq(memberId));

		return PageableExecutionUtils.getPage(favoriteListDtos, pageable, countQuery::fetchOne);

	}

	private void postPictureUrlOnFavoriteList(List<FavoriteListDto> favoriteListDtos) {
		List<Long> favoriteIdList = favoriteListDtos.stream().map(o -> o.getFavoriteId()).collect(Collectors.toList());

		for (Long favoriteId : favoriteIdList) {
			List<Long> spotIdList = queryFactory
				.select(
					favoriteSpot.spot.id
				)
				.from(favoriteSpot)
				.where(favoriteSpot.favorite.id.eq(favoriteId))
				.fetch();

			//이거 spotreposi 에 있는 거 하고 다름 -> 거기에 있는 건 한 관광지에서 사진을 선별하는 거고 여기는 여러 관광지에서 사진하나를 선별하는 것임!
			//또한 위시리스트에 많은 관광지를 담지 않을 것으로 하여 picture과 groupBy, max로 해결함
			//groupby 로 해결, picture 대상으로만 쿼리문을 날려서 굳이 사진이 없는 경우를 고려할 필요가 없다
			System.out.println("=======================");
			List<PictureUrlDto> pictureUrlDtos = queryFactory
				.select(Projections.constructor(PictureUrlDto.class,
						picture.spot.id,
						picture.url
					)
				)
				.from(picture)
				.innerJoin(picture.spot, spot)
				.where(picture.spot.id.in(spotIdList))
				.groupBy(picture.spot.id)
				.limit(3)
				.fetch();
			System.out.println("=======================");

			favoriteListDtos.forEach(sl -> sl.setPictureUrlDtoList(pictureUrlDtos));

		}
	}

	/**
	 * 특정 위시리스트에 넣은 관광지 정보 보여주기
	 * -> 이거 최단 경로 페이지에서 특정 위시리스트에 있는 관광지의 정보 보여줄때 필요한 것임
	 *
	 * @param favoriteId
	 * @return
	 */
	public List<SpotListDtoByFavoriteSpot> favoriteSpotList(Long favoriteId) {

		System.out.println("================================");

		List<FavoriteSpot> spotListFirstDtos = queryFactory
			.select(favoriteSpot
			)
			.from(favoriteSpot)
			.join(favoriteSpot.spot, spot)
			.where(favoriteSpot.favorite.id.eq(favoriteId))
			.fetch();

		System.out.println("================================");
		List<Long> spotIdList = spotListFirstDtos.stream()
			.mapToLong(i -> i.getSpot().getId())
			.boxed()
			.collect(Collectors.toList());
		System.out.println("================================");

		//여기서 페치조인을 하지 않으면 select picture 쿼리 문 엄청 많이 날라 //-> 아래와 같이 페치 조인으로 in절로 spotlist 잡아줌
		//select spot0_.spot_id as spot_id1_7_0_, pictures1_.picture_id as picture_1_4_1_, spot0_.address as address2_7_0_, spot0_.description as descript3_7_0_, spot0_.location as location4_7_0_, spot0_.name as name5_7_0_, spot0_.score_id as score_id6_7_0_, pictures1_.spot_id as spot_id3_4_1_, pictures1_.url as url2_4_1_, pictures1_.spot_id as spot_id3_4_0__, pictures1_.picture_id as picture_1_4_0__ from spot spot0_ inner join picture pictures1_ on spot0_.spot_id=pictures1_.spot_id where spot0_.spot_id in (1 , 3 , 5 , 7 , 9 , 11 , 13 , 15 , 17 , 19 , 21 , 23 , 25 , 27 , 29 , 31 , 33 , 35 , 37 , 39 , 41 , 43 , 45 , 47 , 49 , 51 , 53 , 55 , 57 , 59 , 61 , 63 , 65 , 67 , 69 , 71 , 73 , 75 , 77 , 79 , 81 , 83 , 85 , 87 , 89 , 91 , 93 , 95 , 97 , 99);
		//spot, picture 관계는 1대 다 관계임, 내가 여기서 batch 사이즈를 yml에 잡아줘서 여러개의 spotid 리스트에 있는 picture들 한번에 가져옴
		List<Spot> spotList1 = queryFactory
			.select(spot
			)
			.from(spot)
			.innerJoin(spot.pictures, picture).fetchJoin()
			.where(spot.id.in(spotIdList))
			.fetch();
		System.out.println("================================");
		//만약 패치 조인을 해주지 않으면, 여기부분에서 spot id 당  select picture 쿼리문들이 날아감
		//패치 조인을 하면 여기 == 사이는 추가 쿼리문이 날아가지 않음

		List<SpotListDtoByFavoriteSpot> spotListDtosByFavoriteSpot = new ArrayList<>();

		spotList1.stream().forEach(spot -> {
			if (!spot.getPictures().isEmpty()) {
				spotListDtosByFavoriteSpot.add(
					new SpotListDtoByFavoriteSpot(spot.getId(), spot.getName(), spot.getAddress(),
						spot.getDescription(),
						spot.getPictures().get(0).getUrl()));
			} else {
				spotListDtosByFavoriteSpot.add(
					new SpotListDtoByFavoriteSpot(spot.getId(), spot.getName(), spot.getAddress(),
						spot.getDescription(),
						"Empty"));
			}

		});

		System.out.println("================================");

		return spotListDtosByFavoriteSpot;

	}

	/**
	 * 테스트를 위한 코드
	 * 위시리스트안에 관광지가 존재하냐?
	 *
	 * @param favoriteId
	 * @param routeForm
	 * @return
	 */
	public FavoriteSpot existSpot(Long favoriteId, RouteForm routeForm) {
		FavoriteSpot favoriteSpot = queryFactory
			.selectFrom(QFavoriteSpot.favoriteSpot)
			.where(favoriteIdEq(favoriteId), spot.id.in(routeForm.getSpotIdList()))
			.fetchOne();

		return favoriteSpot;

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






