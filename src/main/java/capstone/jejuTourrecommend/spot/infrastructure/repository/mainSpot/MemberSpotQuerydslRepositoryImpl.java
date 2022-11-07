package capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot;

import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.UserWeightDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
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
import static capstone.jejuTourrecommend.spot.domain.mainSpot.QMemberSpot.memberSpot;

@Repository
@Slf4j
@Transactional
public class MemberSpotQuerydslRepositoryImpl implements MemberSpotQuerydslRepository{

	private final JPAQueryFactory queryFactory;

	public MemberSpotQuerydslRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<SpotListDto> searchSpotByUserPriority(Long memberId, List locationList, Pageable pageable) {
		List<SpotListDto> spotListDtoList = queryFactory
			.select(Projections.constructor(SpotListDto.class,
				memberSpot.spot.id,
				memberSpot.spot.name,
				memberSpot.spot.address,
				memberSpot.spot.description
				)
			)
			.from(memberSpot)
			.innerJoin(memberSpot.spot, spot)
			.where(spot.location.in(locationList), memberEq(memberId))
			.orderBy(memberSpot.score.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(memberSpot.count())
			.from(memberSpot)
			.innerJoin(memberSpot.spot, spot)
			.where(spot.location.in(locationList), memberEq(memberId));
		return PageableExecutionUtils.getPage(spotListDtoList, pageable, countQuery::fetchOne);
	}

	@Override
	public void updateMemberSpotByPriority(Long memberId, UserWeightDto userWeightDto) {
		queryFactory
			.update(memberSpot)
			.set(memberSpot.score,
				getJpqlQuery(userWeightDto)
			)
			.where(memberSpot.member.id.eq(memberId))
			.execute();
	}
	private JPQLQuery<Double> getJpqlQuery(UserWeightDto userWeightDto) {
		return JPAExpressions
			.select(
				spot.score.viewScore.multiply(userWeightDto.getViewWeight())
					.add(spot.score.priceScore.multiply(userWeightDto.getPriceWeight()))
					.add(spot.score.facilityScore.multiply(userWeightDto.getFacilityWeight()))
					.add(spot.score.surroundScore.multiply(userWeightDto.getSurroundWeight()))
					.divide(userWeightDto.getViewWeight() + userWeightDto.getPriceWeight()
						+ userWeightDto.getFacilityWeight() + userWeightDto.getSurroundWeight())
			)
			.from(spot)
			.where(spot.eq(memberSpot.spot));
	}

	private BooleanExpression memberEq(Long memberId) {
		return memberId != null ? memberSpot.member.id.eq(memberId) : null;
	}

}
