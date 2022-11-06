package capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot;

import capstone.jejuTourrecommend.spot.domain.mainSpot.MemberSpot;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.UserWeightDto;
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
import java.util.stream.Collectors;

import static capstone.jejuTourrecommend.authentication.domain.QMember.member;
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
		List<MemberSpot> memberSpotList = queryFactory
			.select(memberSpot)
			.from(memberSpot)
			.innerJoin(memberSpot.spot, spot)
			.where(spot.location.in(locationList), memberEq(memberId))
			.orderBy(memberSpot.score.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		List<SpotListDto> spotListDtoList = memberSpotList.stream().map(o -> new SpotListDto(
				o.getSpot().getId(), o.getSpot().getName(), o.getSpot().getAddress(), o.getSpot().getDescription()))
			.collect(Collectors.toList());

		JPAQuery<Long> countQuery = queryFactory
			.select(memberSpot.count())
			.from(memberSpot)
			.innerJoin(memberSpot.spot, spot)
			.innerJoin(memberSpot.member, member)
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
