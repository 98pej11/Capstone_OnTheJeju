package capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot;

import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.spot.domain.mainSpot.Category;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import com.querydsl.core.types.OrderSpecifier;
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

import static capstone.jejuTourrecommend.spot.domain.QSpot.spot;
import static capstone.jejuTourrecommend.wishList.domain.QFavorite.favorite;

@Repository
@Slf4j
@Transactional
public class SpotQuerydslRepositoryImpl implements SpotQuerydslRepository {

	private final JPAQueryFactory queryFactory;

	public SpotQuerydslRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	/**
	 * 관광지 이름에 따른 조회
	 *
	 * @param spotName
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<SpotListDto> searchBySpotNameContains(Long memberId, String spotName, Pageable pageable) {

		List<SpotListDto> spotListDtoList = queryFactory
			.select(Projections.constructor(SpotListDto.class,
					spot.id,
					spot.name,
					spot.address,
					spot.description
				)
			)
			.from(spot)
			.where(spot.name.contains(spotName))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		if (spotListDtoList.isEmpty()) {
			throw new UserException("검색된 결과가 없습니다");
		}

		JPAQuery<Long> countQuery = queryFactory
			.select(spot.count())
			.from(spot)
			.where(spot.name.contains(spotName));

		return PageableExecutionUtils.getPage(spotListDtoList, pageable, () -> countQuery.fetchOne());

	}

	/**
	 * 사용자가 선택한 location 과 카테고리에 따라 관광지 순위별로 관광지 리스트 보여주기
	 * 조건에 맞는 관광지에 "여러 사진" 보여주기
	 *
	 * @param locationList
	 * @param category
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<SpotListDto> searchSpotByLocationAndCategory(Long memberId, List locationList, Category category,
		Pageable pageable) {

		log.info("location = {}", locationList);
		log.info("category = {}", category);
		OrderSpecifier<Double> orderSpecifier = getDoubleOrderSpecifier(category);

		List<SpotListDto> spotListDtoList = queryFactory
			.select(Projections.constructor(SpotListDto.class,
					spot.id,
					spot.name,
					spot.address,
					spot.description
				)
			)
			.from(spot)
			.where(spot.location.in(locationList))
			.orderBy(orderSpecifier)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(spot.count())
			.from(spot)
			.where(spot.location.in(locationList));

		return PageableExecutionUtils.getPage(spotListDtoList, pageable, () -> countQuery.fetchOne());

	}


	private OrderSpecifier<Double> getDoubleOrderSpecifier(Category category) {
		OrderSpecifier<Double> orderSpecifier;
		// 사용자가 선택한 카테고리에 따라 정렬 조건 선택
		if (category == Category.VIEW)
			orderSpecifier = spot.score.viewScore.desc();
		else if (category == Category.PRICE)
			orderSpecifier = spot.score.priceScore.desc();
		else if (category == Category.FACILITY)
			orderSpecifier = spot.score.facilityScore.desc();
		else if (category == Category.SURROUND)
			orderSpecifier = spot.score.surroundScore.desc();
		else {   //사용자가 카테고리를 선택하지 않았을 때
			log.info(" category = {} ", category);
			orderSpecifier = spot.score.rankAverage.asc();//Todo: 업데이트
		}
		return orderSpecifier;
	}

	private BooleanExpression memberFavoriteEq(Long memberId) {
		return memberId != null ? favorite.member.id.eq(memberId) : null;
	}



}














