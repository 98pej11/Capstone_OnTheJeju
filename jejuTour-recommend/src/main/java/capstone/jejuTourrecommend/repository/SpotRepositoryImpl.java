package capstone.jejuTourrecommend.repository;

import capstone.jejuTourrecommend.domain.*;
import capstone.jejuTourrecommend.web.pageDto.mainPage.QSpotListDto;
import capstone.jejuTourrecommend.web.pageDto.mainPage.SpotListDto;
import capstone.jejuTourrecommend.web.pageDto.mainPage.UserWeightDto;
import capstone.jejuTourrecommend.web.pageDto.spotPage.ScoreDto;
import com.querydsl.core.Tuple;
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

import static capstone.jejuTourrecommend.domain.QFavorite.favorite;
import static capstone.jejuTourrecommend.domain.QFavoriteSpot.favoriteSpot;
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
    public Page<SpotListDto> searchBySpotNameContains(String spotName, Pageable pageable) {


        List<SpotListDto> contents = queryFactory
                .select(
                        new QSpotListDto(
                                spot.id,
                                spot.name,
                                spot.address,
                                spot.description,
                                JPAExpressions  //스칼라 서브커리에서 limit 못 사용함 그래서 max 사용
                                        .select(picture.url.max()) //Todo: 업데이트 부분
                                        .from(picture)
                                        .where(picture.spot.id.eq(spot.id))
                        )
                )
                .from(spot)
                .where(spot.name.contains(spotName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(spot.count())
                .from(spot)
                .where(spot.name.contains(spotName));

        return PageableExecutionUtils.getPage(contents, pageable, () -> countQuery.fetchOne());


    }


    @Override
    public Boolean isFavoriteSpot(Long memberId, Long spotId) {

        List<Long> favoriteList = queryFactory
                .select(favorite.id)
                .from(favorite)
                .where(memberFavoriteEq(memberId))
                .fetch();

        Integer integer = queryFactory
                .selectOne()
                .from(favoriteSpot)
                .where(favoriteSpot.favorite.id.in(favoriteList), favoriteSpot.spot.id.eq(spotId))
                .fetchFirst();

        if (integer==null){
            return false;
        }else {
            return true;
        }


    }



    @Override
    public Page<SpotListDto> searchSpotByLocationAndCategory(List locationList, Category category, Pageable pageable) {


        OrderSpecifier<Double> orderSpecifier;

        log.info("location = {}",locationList);
        log.info("category = {}",category);
        //log.info("offset = {}",pageable.getOffset());
        //log.info("size = {}",pageable.getPageSize());

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
            orderSpecifier = spot.score.rankAverage.asc();//Todo: 업데이트
        }

        List<Tuple> fetch = queryFactory
                .select(spot.id,
                        spot.score.viewScore,
                        spot.score.priceScore,
                        spot.score.facilityScore,
                        spot.score.surroundScore,
                        spot.score.rankAverage
                )
                .from(spot)
                .where(spot.location.in(locationList))
                .orderBy(orderSpecifier)
                .fetch();

        log.info("fetch ={}",fetch);


        List<SpotListDto> contents = queryFactory
                .select(new QSpotListDto(
                                spot.id,
                                spot.name,
                                spot.address,
                                spot.description,
                                JPAExpressions  //스칼라 서브커리에서 limit 못 사용함 그래서 max 사용
                                        .select(picture.url.max()) //Todo: 업데이트 부분
                                        .from(picture)
                                        .where(picture.spot.id.eq(spot.id))
                                //spot.pictures.any().url//패이징할꺼라 일대다 패치조인 안할거임
                                //picture.url

                        )
                )
                .from(spot)
                //.where(locationEq(location))
                .where(spot.location.in(locationList))
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        /**
        List<Tuple> contents = queryFactory
                .select(
                        spot.id,
                        spot.name,
                        spot.address,
                        spot.description
                )
                .from(spot)
                //.where(locationEq(location))
                .where(spot.location.in(locationList))
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<SpotListDto> spotListDtos = new  ArrayList<SpotListDto>();

        for(long i=pageable.getOffset();i< pageable.getOffset() +pageable.getPageSize();i++) {
            int j = (int) i;
            Long spotId = contents.get(j).get(spot.id);

            List<String> urls = queryFactory
                    .select(picture.url)
                    .from(picture)
                    .where(picture.spot.id.eq(spotId))
                    .fetch();


            spotListDtos.add(new SpotListDto(
                    contents.get(j).get(spot.id),contents.get(j).get(spot.name),contents.get(j).get(spot.address),
                    contents.get(j).get(spot.description),urls));


        }*/


        JPAQuery<Long> countQuery = queryFactory
                .select(spot.count())
                .from(spot)
                //.where(locationEq(location))
                .where(spot.location.in(locationList))
                ;

        return PageableExecutionUtils.getPage(contents, pageable, () -> countQuery.fetchOne());
    }

    //가중치가 있을 경우의 쿼리

    @Transactional
    @Override
    public Page<SpotListDto> searchSpotByUserPriority(Long memberId, List locationList, UserWeightDto userWeightDto, Pageable pageable) {



        OrderSpecifier<Double> orderSpecifier=null;

        log.info("memberId = {}",memberId);
        log.info("location = {}",locationList);
        log.info("userWeight = {}",userWeightDto);

        queryFactory
                .update(memberSpot)
                .set(memberSpot.score, 0d)
                .where(memberSpot.member.id.eq(memberId))
                .execute();

        List<Tuple> initScoreList = queryFactory
                .select(memberSpot.score, memberSpot.spot)
                .from(memberSpot)
                .where(memberSpot.member.id.eq(memberId))
                .fetch();

        log.info("initScoreList = {}",initScoreList);


        //지금 가중치를 업데이트 한 것임
        queryFactory
                .update(memberSpot)
                .set(memberSpot.score,
                        getJpqlQuery(userWeightDto)
                        )
                .where(memberSpot.member.id.eq(memberId))
                .execute();

        List<Tuple> updatedScoreList = queryFactory
                .select(memberSpot.score, memberSpot.spot.id)
                .from(memberSpot)
                .where(memberSpot.member.id.eq(memberId))
                .fetch();

        log.info("updatedScoreList = {}",updatedScoreList);


        //지금 점수는 업데이트 했는데 이게 상위 몇퍼센트인지 구할려고 하는 것임

        List<Tuple> fetch = queryFactory
                .select(
                        spot.id, spot.score.viewScore, spot.score.priceScore, spot.score.facilityScore, spot.score.surroundScore,
                        //userWeightDto.getViewWeight(),userWeightDto.getPriceWeight(),
                        spot.score.viewScore.multiply(userWeightDto.getViewWeight())
                                .add(spot.score.priceScore.multiply(userWeightDto.getPriceWeight()))
                                .add(spot.score.facilityScore.multiply(userWeightDto.getFacilityWeight()))
                                .add(spot.score.surroundScore.multiply(userWeightDto.getSurroundWeight()))
                                .divide(userWeightDto.getViewWeight() + userWeightDto.getPriceWeight()
                                        + userWeightDto.getFacilityWeight() + userWeightDto.getSurroundWeight())
                )
                .from(spot)
                .fetch();
        log.info("fetch = {}",fetch);



        List<SpotListDto> contents = queryFactory
                .select(new QSpotListDto(
                                memberSpot.spot.id,
                                memberSpot.spot.name,
                                memberSpot.spot.address,
                                memberSpot.spot.description,
                                JPAExpressions   //스칼라 서브커리에서 limit 못 사용함 그래서 max 사용
                                        .select(picture.url.max())  //Todo: 업데이트부분
                                        .from(picture)
                                        .where(picture.spot.id.eq(memberSpot.spot.id))
                        )
                )
                .from(memberSpot)
                .where(spot.location.in(locationList),memberEq(memberId))
                .orderBy(memberSpot.score.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        log.info("contents = {}",contents);

        JPAQuery<Long> countQuery = queryFactory
                .select(memberSpot.count())
                .from(memberSpot)
                .where(spot.location.in(locationList),memberEq(memberId));

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




    private JPQLQuery<Double> getJpqlQuery(UserWeightDto userWeightDto) {
        JPQLQuery<Double> updatedScore = JPAExpressions
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

        log.info("updatedScore = {}",updatedScore);

        return updatedScore;


    }

    private BooleanExpression memberFavoriteEq(Long memberId){
        return memberId != null ? favorite.member.id.eq(memberId) : null;
    }

    private BooleanExpression favoriteListEq(List<Long> favoriteList){
        return favoriteList != null  ? favoriteSpot.favorite.id.in(favoriteList) :  null;
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














