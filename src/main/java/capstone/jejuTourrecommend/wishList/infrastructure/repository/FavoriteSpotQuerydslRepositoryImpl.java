package capstone.jejuTourrecommend.wishList.infrastructure.repository;

import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

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

	@Override
	public List<Long> getSpotIdByFavoriteSpot(Long memberId, List<Long> spotIdList) {
		return queryFactory
			.select(favoriteSpot.spot.id)
			.from(favoriteSpot)
			.where(favoriteSpot.favorite.member.id.eq(memberId), favoriteSpot.spot.id.in(spotIdList))
			.fetch();
	}

	@Override
	public List<Long> getSpotIdList(Long favoriteId, List<Long> spotIdList) {

		List<Long> spotIdListByFavoriteIdAndSpotIdList = queryFactory
			.select(favoriteSpot.spot.id)
			.from(favoriteSpot)
			.innerJoin(favoriteSpot.spot, spot)
			.where(favoriteIdEq(favoriteId), spot.id.in(spotIdList))
			.fetch();

		if (isEmpty(spotIdListByFavoriteIdAndSpotIdList)) {
			throw new UserException("모든 spotId가 위시리스트에 있는 spotId가 아닙니다");
		}
		return spotIdListByFavoriteIdAndSpotIdList;
	}

	private BooleanExpression favoriteIdEq(Long favoriteId) {
		return isEmpty(favoriteId) ? null : favoriteSpot.favorite.id.eq(favoriteId);
	}

}






