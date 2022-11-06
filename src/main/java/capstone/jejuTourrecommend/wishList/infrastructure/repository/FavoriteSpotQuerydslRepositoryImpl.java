package capstone.jejuTourrecommend.wishList.infrastructure.repository;

import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.wishList.domain.dto.ScoreSumDto;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.RouteForm;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static capstone.jejuTourrecommend.spot.domain.QScore.score;
import static capstone.jejuTourrecommend.spot.domain.QSpot.spot;
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
	 * 특정 위시리시트에 있는 관광지 리스트 조회
	 *
	 * @param favoriteId
	 * @param routeForm
	 * @return
	 */
	public List<Long> getSpotIdList(Long favoriteId, RouteForm routeForm) {
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
	public ScoreSumDto getScoreSumDto(List<Long> spotIdList) {

		List<ScoreSumDto> scoreSumDtos = queryFactory
			.select(
				Projections.constructor(
					ScoreSumDto.class,
					spot.score.viewScore.sum(),
					spot.score.priceScore.sum(),
					spot.score.facilityScore.sum(),
					spot.score.surroundScore.sum()
				)
			)
			.from(spot)
			.innerJoin(spot.score, score)
			.where(spot.id.in(spotIdList))
			.fetch();

		return scoreSumDtos.get(0);
	}

	private BooleanExpression favoriteIdEq(Long favoriteId) {
		return isEmpty(favoriteId) ? null : favoriteSpot.favorite.id.eq(favoriteId);
	}

}






