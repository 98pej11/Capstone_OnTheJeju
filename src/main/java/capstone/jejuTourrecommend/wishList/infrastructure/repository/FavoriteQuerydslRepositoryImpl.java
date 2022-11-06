package capstone.jejuTourrecommend.wishList.infrastructure.repository;

import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static capstone.jejuTourrecommend.authentication.domain.QMember.member;
import static capstone.jejuTourrecommend.wishList.domain.QFavorite.favorite;
import static capstone.jejuTourrecommend.wishList.domain.QFavoriteSpot.favoriteSpot;

@Repository
@Slf4j
@Transactional
public class FavoriteQuerydslRepositoryImpl implements FavoriteQuerydslRepository{

	private final JPAQueryFactory queryFactory;

	public FavoriteQuerydslRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	/**
	 * 사용자 위시리스트에 특정 관광지가 포함되어 있는가?
	 *
	 * @param memberId
	 * @param spotId
	 * @return
	 */
	@Override
	public Boolean isFavoriteSpot(Long memberId, Long spotId) {

		//회원의 위시리스트 아이디 목록
		List<Long> favoriteList = queryFactory
			.select(favorite.id)
			.from(favorite)
			.where(memberFavoriteEq(memberId))
			.fetch();

		//회원 위시리스트에 특정 관광지가 있는지 하나만 조회- exit 를 사용하면 성능상 느려서 fetchFirst 로 조회함
		Integer isExit = queryFactory
			.selectOne()
			.from(favoriteSpot)
			.where(favoriteSpot.favorite.id.in(favoriteList), favoriteSpot.spot.id.eq(spotId))
			.fetchFirst();

		if (isExit == null) {
			return false;
		} else {
			return true;
		}

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

		JPAQuery<Long> countQuery = queryFactory
			.select(favorite.count())
			.from(favorite)
			.innerJoin(favorite.member, member)
			.where(favorite.member.id.eq(memberId));

		return PageableExecutionUtils.getPage(favoriteListDtos, pageable, countQuery::fetchOne);

	}



	private BooleanExpression memberFavoriteEq(Long memberId) {
		return memberId != null ? favorite.member.id.eq(memberId) : null;
	}

}
