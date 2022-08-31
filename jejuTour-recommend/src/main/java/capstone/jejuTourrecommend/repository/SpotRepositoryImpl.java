package capstone.jejuTourrecommend.repository;

import capstone.jejuTourrecommend.domain.*;
import capstone.jejuTourrecommend.web.pageDto.mainPage.*;
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
import java.util.Map;
import java.util.stream.Collectors;

import static capstone.jejuTourrecommend.domain.QFavorite.favorite;
import static capstone.jejuTourrecommend.domain.QFavoriteSpot.favoriteSpot;
import static capstone.jejuTourrecommend.domain.QMember.member;
import static capstone.jejuTourrecommend.domain.QMemberSpot.memberSpot;
import static capstone.jejuTourrecommend.domain.QPicture.picture;
import static capstone.jejuTourrecommend.domain.QScore.score;
import static capstone.jejuTourrecommend.domain.QSpot.spot;


@Slf4j
@Transactional
public class SpotRepositoryImpl implements SpotRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public SpotRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }


    /**
     * 관광지 이름에 따른 조회
     * @param spotName
     * @param pageable
     * @return
     */
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


    /**
     * 사용자 위시리스트에 특정 관광지가 포함되어 있는가?
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

        if (isExit==null){
            return false;
        }else {
            return true;
        }


    }


    /**
     * 사용자가 선태가한 location 과 카테고리에 따라 관광지 순위별로 관광지 리스트 보여주기
     * @param locationList
     * @param category
     * @param pageable
     * @return
     */
    @Override
    public Page<SpotListDto> searchSpotByLocationAndCategory(List locationList, Category category, Pageable pageable) {


        log.info("location = {}",locationList);
        log.info("category = {}",category);
        //log.info("offset = {}",pageable.getOffset());
        //log.info("size = {}",pageable.getPageSize());


        OrderSpecifier<Double> orderSpecifier = getDoubleOrderSpecifier(category);

        //테스트용: 사용자가 선택한 지역과 카테고리로 정렬한 관광지의 점수 출력하기
        List<Tuple> fetch = queryFactory
                .select(spot.id,
                        spot.score.viewScore,
                        spot.score.priceScore,
                        spot.score.facilityScore,
                        spot.score.surroundScore,
                        spot.score.rankAverage
                )
                .from(spot)
                .leftJoin(spot.score, score)
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



        JPAQuery<Long> countQuery = queryFactory
                .select(spot.count())
                .from(spot)
                //.where(locationEq(location))
                .where(spot.location.in(locationList));

        return PageableExecutionUtils.getPage(contents, pageable, () -> countQuery.fetchOne());
    }

    /**
     * 조건에 맞는 관광지에 "여러 사진" 보여주기
     * @param locationList
     * @param category
     * @param pageable
     * @return
     */
    @Override
    public Page<OptimizationSpotListDto> optimizationSearchSpotByLocationAndCategory(List locationList, Category category, Pageable pageable) {

        log.info("location = {}",locationList);
        log.info("category = {}",category);
        //log.info("offset = {}",pageable.getOffset());
        //log.info("size = {}",pageable.getPageSize());
        OrderSpecifier<Double> orderSpecifier = getDoubleOrderSpecifier(category);


        List<OptimizationSpotListDto> optimizationSpotListDtoList = queryFactory
                .select(Projections.constructor(OptimizationSpotListDto.class,
                                spot.id,
                                spot.name,
                                spot.address,
                                spot.description
                        )
                )
                .from(spot)
                //.where(locationEq(location))
                .where(spot.location.in(locationList))
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        postSpotPictureUrlstoDTO(optimizationSpotListDtoList);

        JPAQuery<Long> countQuery = queryFactory
                .select(spot.count())
                .from(spot)
                //.where(locationEq(location))
                .where(spot.location.in(locationList));

        return PageableExecutionUtils.getPage(optimizationSpotListDtoList, pageable, () -> countQuery.fetchOne());


    }


    /**
     * 사용자의 가중치에 따른 관광지 조회
     * @param memberId
     * @param locationList
     * @param userWeightDto
     * @param pageable
     * @return
     */
    @Transactional
    @Override
    public Page<SpotListDto> searchSpotByUserPriority(Long memberId, List locationList, UserWeightDto userWeightDto, Pageable pageable) {


        log.info("memberId = {}",memberId);
        log.info("location = {}",locationList);
        log.info("userWeight = {}",userWeightDto);


        //지금 가중치를 업데이트 한 것임
        queryFactory
                .update(memberSpot)
                .set(memberSpot.score,
                        getJpqlQuery(userWeightDto)
                        )
                .where(memberSpot.member.id.eq(memberId))
                .execute();

        //업데이트된 점수 확인용 쿼리
//        List<Tuple> updatedScoreList = queryFactory
//                .select(memberSpot.score, memberSpot.spot.id)
//                .from(memberSpot)
//                .where(memberSpot.member.id.eq(memberId))
//                .fetch();
//
//        log.info("updatedScoreList = {}",updatedScoreList);


        //위에서 업데이트 했는데 이게 상위 몇퍼센트인지 확인용 쿼리
//        List<Tuple> fetch = queryFactory
//                .select(
//                        spot.id, spot.score.viewScore, spot.score.priceScore, spot.score.facilityScore, spot.score.surroundScore,
//                        //userWeightDto.getViewWeight(),userWeightDto.getPriceWeight(),
//                        getJpqlQuery(userWeightDto)
//                )
//                .from(spot)
//                .fetch();
//
//        log.info("fetch = {}",fetch);



        List<SpotListDto> contents = queryFactory
                .select(new QSpotListDto(
                                memberSpot.spot.id,
                                memberSpot.spot.name,
                                memberSpot.spot.address,
                                memberSpot.spot.description,
                                JPAExpressions   //스칼라 서브커리에서 limit 못 사용함 그래서 max 사용
                                        .select(picture.url.max())
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
                .innerJoin(memberSpot.spot, spot)
                .innerJoin(memberSpot.member, member)
                .where(spot.location.in(locationList),memberEq(memberId));

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);

    }


    @Override
    public Page<OptimizationSpotListDto> optimizationSearchSpotByUserPriority(Long memberId, List locationList, UserWeightDto userWeightDto, Pageable pageable) {


        log.info("memberId = {}",memberId);
        log.info("location = {}",locationList);
        log.info("userWeight = {}",userWeightDto);


        //지금 가중치를 업데이트 한 것임
        queryFactory
                .update(memberSpot)
                .set(memberSpot.score,
                        getJpqlQuery(userWeightDto)
                )
                .where(memberSpot.member.id.eq(memberId))
                .execute();


        List<MemberSpot> memberSpotList = queryFactory
                .select(memberSpot)
                .from(memberSpot)
                .leftJoin(memberSpot.spot, spot).fetchJoin()
                .where(spot.location.in(locationList), memberEq(memberId))
                .orderBy(memberSpot.score.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<OptimizationSpotListDto> optimizationSpotListDtoList = memberSpotList.stream().map(o -> new OptimizationSpotListDto(
                        o.getSpot().getId(), o.getSpot().getName(), o.getSpot().getAddress(), o.getSpot().getDescription()))
                .collect(Collectors.toList());

        postSpotPictureUrlstoDTO(optimizationSpotListDtoList);


        JPAQuery<Long> countQuery = queryFactory
                .select(memberSpot.count())
                .from(memberSpot)
                .innerJoin(memberSpot.spot, spot)
                .innerJoin(memberSpot.member, member)
                .where(spot.location.in(locationList),memberEq(memberId));

        return PageableExecutionUtils.getPage(optimizationSpotListDtoList, pageable, countQuery::fetchOne);


    }

    private void postSpotPictureUrlstoDTO(List<OptimizationSpotListDto> optimizationSpotListDtoList) {

        List<Long> spotIdList = optimizationSpotListDtoList.stream().map(o -> o.getSpotId()).collect(Collectors.toList());

        List<PictureDetailDto> pictureDetailDtoList = queryFactory
                .select(Projections.constructor(PictureDetailDto.class,
                                picture.id,
                                picture.url,
                                picture.spot.id
                        )
                )
                .from(picture)
                .innerJoin(picture.spot, spot)
                .where(picture.spot.id.in(spotIdList))
                .fetch();


        Map<Long, List<PictureDetailDto>> collect1 = pictureDetailDtoList.stream().collect(Collectors.groupingBy(p -> p.getSpotId()));

        optimizationSpotListDtoList.forEach(sl -> sl.setPictureDetailDtoList(collect1.get(sl.getSpotId())));
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
                .leftJoin(QSpot.spot.score, score)
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

    private OrderSpecifier<Double> getDoubleOrderSpecifier(Category category) {
        OrderSpecifier<Double> orderSpecifier;
        // 사용자가 선택한 카테고리에 따라 정렬 조건 선택
        if(category ==Category.VIEW)
            orderSpecifier = spot.score.viewScore.desc();
        else if (category ==Category.PRICE)
            orderSpecifier = spot.score.priceScore.desc();
        else if(category ==Category.FACILITY)
            orderSpecifier = spot.score.facilityScore.desc();
        else if(category ==Category.SURROUND)
            orderSpecifier = spot.score.surroundScore.desc();
        else{   //사용자가 카테고리를 선택하지 않았을 때
            log.info(" category = {} ", category);
            orderSpecifier = spot.score.rankAverage.asc();//Todo: 업데이트
        }
        return orderSpecifier;
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














