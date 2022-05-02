package capstone.jejuTourrecommend.repository;

import capstone.jejuTourrecommend.domain.*;
import capstone.jejuTourrecommend.web.mainPage.*;
import capstone.jejuTourrecommend.web.spotPage.ScoreDto;
import com.querydsl.core.types.OrderSpecifier;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static capstone.jejuTourrecommend.domain.QMember.member;
import static capstone.jejuTourrecommend.domain.QMemberSpot.*;
import static capstone.jejuTourrecommend.domain.QPicture.*;
import static capstone.jejuTourrecommend.domain.QSpot.spot;


@Slf4j
@Transactional
public class SpotRepositoryImpl implements SpotRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public SpotRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<SpotListDto> searchSpotByLocationAndCategory(Location location, Category category, Pageable pageable) {

        OrderSpecifier<Double> orderSpecifier;

        log.info("location = {}",location);
        log.info("category = {}",category);

        if(category==Category.VIEW)
            orderSpecifier = spot.score.viewScore.desc();
        else if (category==Category.PRICE)
            orderSpecifier = spot.score.priceScore.desc();
        else if(category==Category.FACILITY)
            orderSpecifier = spot.score.facilityScore.desc();
        else if(category==Category.SURROUND)
            orderSpecifier = spot.score.surroundScore.desc();
        else{
            log.info(" category = {} ",category);
            orderSpecifier = spot.score.rankAverage.desc();
        }

        List<SpotListDto> contents = queryFactory
                .select(new QSpotListDto(
                                spot.id,
                                spot.name,
                                spot.address,
                                spot.description,
                                JPAExpressions
                                        .select(picture.url.max())//스칼라 서브커리에서 limit 못 사용함 그래서 max 사용
                                        .from(picture)
                                        .where(picture.spot.id.eq(spot.id))
                                //spot.pictures.any().url//패이징할꺼라 일대다 패치조인 안할거임
                                //picture.url
                        )
                )
                .from(spot)
                .where(locationEq(location))
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        JPAQuery<Long> countQuery = queryFactory
                .select(spot.count())
                .from(spot)
                .where(locationEq(location));

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    @Transactional
    @Override
    public Page<SpotListDto> searchSpotByUserPriority(Long memberId, Location location, UserWeightDto userWeightDto, Pageable pageable) {

        OrderSpecifier<Double> orderSpecifier=null;

        log.info("memberId = {}",memberId);
        log.info("location = {}",location);
        log.info("userWeight = {}",userWeightDto);



        queryFactory
                .update(memberSpot)
                .set(memberSpot.score,
                        getJpqlQuery(userWeightDto)
                        )
                .where(memberSpot.member.id.eq(memberId))
                .execute();


        List<SpotListDto> contents = queryFactory
                .select(new QSpotListDto(
                                memberSpot.spot.id,
                                memberSpot.spot.name,
                                memberSpot.spot.address,
                                memberSpot.spot.description,
                                JPAExpressions
                                        .select(picture.url.max())//스칼라 서브커리에서 limit 못 사용함 그래서 max 사용
                                        .from(picture)
                                        .where(picture.spot.id.eq(memberSpot.spot.id))
                        )
                )
                .from(memberSpot)
                .where(location1Eq(location),memberEq(memberId))
                .orderBy(memberSpot.score.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(memberSpot.count())
                .from(memberSpot)
                .where(location1Eq(location),memberEq(memberId));

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);

    }

    @Override
    public ScoreDto searchScore(Spot spot) {

        ScoreDto scoreDto = queryFactory
                .select(Projections.constructor(ScoreDto.class,
                        QSpot.spot.score.id,
                        QSpot.spot.score.viewScore,
                        QSpot.spot.score.priceScore,
                        QSpot.spot.score.facilityScore,
                        QSpot.spot.score.surroundScore,

                        QSpot.spot.score.viewRank,
                        QSpot.spot.score.priceRank,
                        QSpot.spot.score.facilityScore,
                        QSpot.spot.score.surroundRank
                ))
                .from(QSpot.spot)
                .where(QSpot.spot.eq(spot))
                .fetchOne();

        return scoreDto;

    }


//    @Override
//    public Page<SpotDetailDto> searchSpotDetail(String spotName) {
//    }


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


    private BooleanExpression locationEq(Location location) {
         return location != null ? spot.location.eq(location) : null;
    }

    private BooleanExpression location1Eq(Location location) {
         return location != null ? memberSpot.spot.location.eq(location) : null;
    }
    private BooleanExpression memberEq(Long memberId){
        return memberId != null ? memberSpot.member.id.eq(memberId) : null;
    }


}














