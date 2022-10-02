package capstone.jejuTourrecommend.repository;

import capstone.jejuTourrecommend.domain.*;
import capstone.jejuTourrecommend.web.pageDto.mainPage.*;
import capstone.jejuTourrecommend.web.pageDto.spotPage.ScoreDto;
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
import java.util.Set;
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
public class SpotRepositoryImpl implements SpotRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public SpotRepositoryImpl(EntityManager em) {
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


        List<Long> spotIdList = spotListDtoList.stream().map(o -> o.getSpotId()).collect(Collectors.toList());


        getBooleanFavoriteSpot(memberId, spotListDtoList, spotIdList);


        postSpotPictureUrlsToDto(spotListDtoList);


        JPAQuery<Long> countQuery = queryFactory
                .select(spot.count())
                .from(spot)
                .where(spot.name.contains(spotName));

        return PageableExecutionUtils.getPage(spotListDtoList, pageable, () -> countQuery.fetchOne());


    }


    /**
     * 사용자의 가중치에 따른 관광지 조회
     *
     * @param memberId
     * @param locationList
     * @param userWeightDto
     * @param pageable
     * @return
     */
    @Override
    public Page<SpotListDto> searchSpotByUserPriority(Long memberId, List locationList, UserWeightDto userWeightDto, Pageable pageable) {


        log.info("memberId = {}", memberId);
        log.info("location = {}", locationList);
        log.info("userWeight = {}", userWeightDto);


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
                .innerJoin(memberSpot.spot, spot).fetchJoin()
                .where(spot.location.in(locationList), memberEq(memberId))
                .orderBy(memberSpot.score.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        List<SpotListDto> spotListDtoList = memberSpotList.stream().map(o -> new SpotListDto(
                        o.getSpot().getId(), o.getSpot().getName(), o.getSpot().getAddress(), o.getSpot().getDescription()))
                .collect(Collectors.toList());

        List<Long> spotIdList = memberSpotList.stream().map(o -> o.getSpot().getId()).collect(Collectors.toList());


        getBooleanFavoriteSpot(memberId, spotListDtoList, spotIdList);

        postSpotPictureUrlsToDto(spotListDtoList);


        JPAQuery<Long> countQuery = queryFactory
                .select(memberSpot.count())
                .from(memberSpot)
                .innerJoin(memberSpot.spot, spot)
                .innerJoin(memberSpot.member, member)
                .where(spot.location.in(locationList), memberEq(memberId));

        return PageableExecutionUtils.getPage(spotListDtoList, pageable, countQuery::fetchOne);


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
    public Page<SpotListDto> searchSpotByLocationAndCategory(Long memberId, List locationList, Category category, Pageable pageable) {

        log.info("location = {}", locationList);
        log.info("category = {}", category);
        //log.info("offset = {}",pageable.getOffset());
        //log.info("size = {}",pageable.getPageSize());
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
                //.where(locationEq(location))
                .where(spot.location.in(locationList))
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        //찜한 유무 포함
        //favortiespot 에서 spotid 들로 데티어 가져옴
        //데이터가 있으면 찜 한거에 있는거고, 없음 찜한 목록에 없는 것임
        //이것 한 dto에 담아서 줘야함
        //isExit는 count>0으로 해서 성능 이슈가 있음 그래서  limit(1)으로 해결하든 해야함

        List<Long> spotIdList = spotListDtoList.stream().map(o -> o.getSpotId()).collect(Collectors.toList());


        getBooleanFavoriteSpot(memberId, spotListDtoList, spotIdList);


        postSpotPictureUrlsToDto(spotListDtoList);

        JPAQuery<Long> countQuery = queryFactory
                .select(spot.count())
                .from(spot)
                //.where(locationEq(location))
                .where(spot.location.in(locationList));

        return PageableExecutionUtils.getPage(spotListDtoList, pageable, () -> countQuery.fetchOne());


    }

    private void postSpotPictureUrlsToDto(List<SpotListDto> spotListDtoList) {

        List<Long> spotIdList = spotListDtoList.stream().map(o -> o.getSpotId()).collect(Collectors.toList());

//        for (Long spotId : spotIdList) {
//
//            List<PictureDetailDto> pictureDetailDtoList = queryFactory
//                    .select(Projections.constructor(PictureDetailDto.class,
//                                    picture.id,
//                                    picture.url,
//                                    picture.spot.id
//                            )
//                    )
//                    .from(picture)
//                    .innerJoin(picture.spot, spot)
//                    .where(picture.spot.id.eq(spotId))
//                    .limit(4)
//                    .fetch();
//
//            spotListDtoList.stream().filter(s -> s.getSpotId() == spotId)
//                    .forEach(s -> s.setPictureDetailDtoList(pictureDetailDtoList));
//
//        }

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

        spotListDtoList.forEach(sl -> sl.setPictureDetailDtoList(collect1.get(sl.getSpotId())));
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


    private void getBooleanFavoriteSpot(Long memberId, List<SpotListDto> spotListDtoList, List<Long> spotIdList) {

        List<Long> favoriteSpotIdList = queryFactory
                .select(
                        favoriteSpot.spot.id
                )
                .from(favoriteSpot)
                .where(favoriteSpot.favorite.member.id.eq(memberId), favoriteSpot.spot.id.in(spotIdList))
                .fetch();

        //list 에서 contain 으로 접근하는 것보다 hashset 으로 접근하는게 더 빨라서 set 으로 stream 해줌 (디버깅해보니깐 hashset 으로 들어감)
        //또한 중복된 spotid가 있음 여러개 위시리스트에서 같은 spot 이 있을수 도 있잖음 그래서 set 을 사용하는게 올바른 것임
        Set<Long> favoriteSpotIdSet = favoriteSpotIdList.stream().collect(Collectors.toSet());

        //"존재 하는 것"과 , 따로 spot 리스트 만들고,
        spotListDtoList.stream().filter(i -> favoriteSpotIdSet.contains(i.getSpotId())).forEach(o -> o.setFavorite(true));

        //"존재하지 않는 것"은 spotId와 매칭되지 않는 것도 map 으로 매핑해서 false 값으 입력해줌->ㄴㄴ 기존꺼 활용해서 !로 filter 로 거름
        spotListDtoList.stream().filter(i -> !favoriteSpotIdSet.contains(i.getSpotId())).forEach(o -> o.setFavorite(false));


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
                .innerJoin(QSpot.spot.score, score)
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

        log.info("updatedScore = {}", updatedScore);

        return updatedScore;


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

    private BooleanExpression favoriteListEq(List<Long> favoriteList) {
        return favoriteList != null ? favoriteSpot.favorite.id.in(favoriteList) : null;
    }


    private BooleanExpression locationEq(Location location) {
        return location != null ? spot.location.eq(location) : null;
    }

    private BooleanExpression location1Eq(Location location) {
        return location != null ? memberSpot.spot.location.eq(location) : null;
    }

    private BooleanExpression memberEq(Long memberId) {
        return memberId != null ? memberSpot.member.id.eq(memberId) : null;
    }


}














