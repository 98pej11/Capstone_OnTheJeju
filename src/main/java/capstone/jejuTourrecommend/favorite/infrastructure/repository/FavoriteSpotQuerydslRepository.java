package capstone.jejuTourrecommend.favorite.infrastructure.repository;


import capstone.jejuTourrecommend.common.exceptionClass.UserException;

import capstone.jejuTourrecommend.favorite.domain.FavoriteSpot;
import capstone.jejuTourrecommend.favorite.domain.QFavoriteSpot;
import capstone.jejuTourrecommend.favorite.domain.dto.FavoriteListDto;
import capstone.jejuTourrecommend.favorite.domain.dto.SpotListDtoByFavoriteSpot;
import capstone.jejuTourrecommend.route.domain.dto.QRouteSpotListDto;
import capstone.jejuTourrecommend.route.domain.dto.RouteSpotListDto;
import capstone.jejuTourrecommend.route.presentation.dto.request.RouteForm;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.mainSpot.Location;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.PictureDetailDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import static capstone.jejuTourrecommend.authentication.domain.QMember.member;
import static capstone.jejuTourrecommend.favorite.domain.QFavorite.favorite;
import static capstone.jejuTourrecommend.favorite.domain.QFavoriteSpot.favoriteSpot;
import static capstone.jejuTourrecommend.spot.domain.QScore.score;
import static capstone.jejuTourrecommend.spot.domain.QSpot.spot;
import static capstone.jejuTourrecommend.spot.domain.detailSpot.QPicture.picture;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
@Slf4j
public class FavoriteSpotQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public FavoriteSpotQuerydslRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 사용자 위시리스트 보여주기
     *
     * @param memberId
     * @param pageable
     * @return
     */
    @Transactional
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



        List<Long> favoriteIdList = favoriteListDtos.stream().map(o -> o.getFavoriteId()).collect(Collectors.toList());

//        List<FavoriteSpot> favoriteSpotList = queryFactory
//                .selectFrom(favoriteSpot)
//                .where(favoriteSpot.favorite.id.in(favoriteIdList))
//                .leftJoin(favoriteSpot.favorite, favorite)
//                .leftJoin(favoriteSpot.spot, spot).fetchJoin()
//                .fetch();
//
//        Map<Long, List<FavoriteSpot>> favoriteSpotListByFavoriteId = favoriteSpotList.stream().collect(Collectors.groupingBy(o -> o.getFavorite().getId()));
//
//        favoriteSpotListByFavoriteId.
//


        for (Long favoriteId : favoriteIdList) {
            List<Long> spotIdList = queryFactory
                    .select(
                            favoriteSpot.spot.id
                    )
                    .from(favoriteSpot)
                    .where(favoriteSpot.favorite.id.eq(favoriteId))
                    .fetch();

            for (Long spotId : spotIdList) {

                List<PictureDetailDto> pictureDetailDtoList = queryFactory
                        .select(
                                Projections.constructor(
                                        PictureDetailDto.class,
                                        picture.id,
                                        picture.url,
                                        picture.spot.id
                                )
                        )
                        .from(picture)
                        .innerJoin(picture.spot, spot)
                        .where(picture.spot.id.eq(spotId))
                        .limit(3)
                        .fetch();

                favoriteListDtos.stream().filter(o-> o.getFavoriteId()==favoriteId)
                        .forEach(o->o.getPictureDetailDtoListBySpotId().add(pictureDetailDtoList));
            }


        }


        JPAQuery<Long> countQuery = queryFactory
                .select(favorite.count())
                .from(favorite)
                .innerJoin(favorite.member, member)
                .where(favorite.member.id.eq(memberId));


        return PageableExecutionUtils.getPage(favoriteListDtos, pageable, countQuery::fetchOne);

    }


    /**
     * 특정 위시리스트에 넣은 관광지 정보 보여주기
     *
     * @param favoriteId
     * @return
     */
    @Transactional
    public List<SpotListDtoByFavoriteSpot> favoriteSpotList(Long favoriteId) {

        //서브 쿼리 성능 속도가 느려서 아래와 같이 쿼리문을 2개로 쪼개서 함
//        List<FavoriteSpotListDto> favoriteSpotListDtos = queryFactory
//                .select(
//                        new QFavoriteSpotListDto(
//                                spot.id,
//                                spot.name,
//                                spot.address,
//                                spot.description,
//                                JPAExpressions
//                                        .select(picture.url.max())//스칼라 서브커리에서 limit 못 사용함 그래서 max 사용
//                                        .from(picture)
//                                        .where(picture.spot.id.eq(favoriteSpot.spot.id))
//
//                        )
//                )
//                .from(favoriteSpot)
//                .join(favoriteSpot.spot,spot)//명시적 조인
//                //.join(favoriteSpot.spot, spot).fetchJoin()
//                .where(favoriteSpot.favorite.id.eq(favoriteId))
//                .fetch();


        List<FavoriteSpot> spotListFirstDtos = queryFactory
                .select(favoriteSpot
                )
                .from(favoriteSpot)
                .join(favoriteSpot.spot, spot).fetchJoin()
                .where(favoriteSpot.favorite.id.eq(favoriteId))
                .fetch();

        List<Long> spotList = spotListFirstDtos.stream().mapToLong(i -> i.getSpot().getId()).boxed().collect(Collectors.toList());

        List<Spot> spotList1 = queryFactory
                .select(spot
                )
                .from(spot)
                .innerJoin(spot.pictures, picture).fetchJoin()
                //.on(spot.id.eq(picture.spot.id))//페치 조인에서는 조인 대상을 필터링(on, where 사용) 할수 없음
                .where(spot.id.in(spotList))
                .fetch();

        List<SpotListDtoByFavoriteSpot> spotListDtos1ByFavoriteSpot = spotList1.stream()
                .map(spot1 ->
                        new SpotListDtoByFavoriteSpot(spot1.getId(), spot1.getName(), spot1.getAddress(), spot1.getDescription(),
                                spot1.getPictures().get(0).getUrl()))
                .collect(Collectors.toList());


        return spotListDtos1ByFavoriteSpot;

    }

    /**
     * 테스트를 위한 코드
     * 위시리스트안에 관광지가 존재하냐?
     *
     * @param favoriteId
     * @param routeForm
     * @return
     */
    @Transactional
    public FavoriteSpot existSpot(Long favoriteId, RouteForm routeForm) {
        FavoriteSpot favoriteSpot = queryFactory
                .selectFrom(QFavoriteSpot.favoriteSpot)
                .where(favoriteIdEq(favoriteId), spot.id.in(routeForm.getSpotIdList()))
                .fetchOne();

        return favoriteSpot;

    }

    /**
     * 사용자가 선호하는 우선순위 카테고리에 따라, 최단 경로 주변 관광지 추천해주기
     *
     * @param favoriteId
     * @param routeForm
     * @return
     */
    @Transactional
    public List recommendSpotList(Long favoriteId, RouteForm routeForm) {

        // 특정 위시리시트에 있는 관광지 리스트 조회
        List<Spot> spotList = queryFactory
                .select(favoriteSpot.spot)
                .from(favoriteSpot)
                .innerJoin(favoriteSpot.spot, spot)//명시적 조인// 근데 여기서는 명시적 조인은 안해도 "cross join"이 안 나감. 알아서 최적화를 해줌
                .where(favoriteIdEq(favoriteId), spot.id.in(routeForm.getSpotIdList()))
                .fetch();

        if (isEmpty(spotList)) {
            log.info("spotList = {}", spotList);
            throw new UserException("모든 spotId가 위시리스트에 있는 spotId가 아닙니다");
        }

        //특정 위시리스트에 있는 각 관광지들의 카테고리별 합산 점수 조회
        List<Tuple> tupleList = queryFactory
                .select(
                        spot.score.viewScore.sum(),
                        spot.score.priceScore.sum(),
                        spot.score.facilityScore.sum(),
                        spot.score.surroundScore.sum()
                )
                .from(spot)
                .innerJoin(spot.score, score)
                .where(spot.in(spotList))
                .fetch();

        //위시리스트에 있느 관광지들이 속한 지역들 리스트
        List<Location> locationList = queryFactory
                .select(spot.location).distinct()
                .from(spot)
                .where(spot.in(spotList))
                .fetch();


        //최대값을 가진 카테고리 구하기
        Tuple tuple = tupleList.get(0);

        Double[] score = new Double[4];
        score[0] = tuple.get(spot.score.viewScore.sum());
        log.info("score[0] = {}", score[0]);
        score[1] = tuple.get(spot.score.priceScore.sum());
        score[2] = tuple.get(spot.score.facilityScore.sum());
        score[3] = tuple.get(spot.score.surroundScore.sum());

        Double max = score[0];
        int j = 0;

        for (int i = 0; i < 4; i++) {
            if (max < score[i]) {
                j = i;
                max = score[i];
            }
        }

        //최대값을 가진 인덱스를 찾아서 해당 카테고리벼로 내림 차순을 할 것임
        OrderSpecifier<Double> orderSpecifier;
        if (j == 0)
            orderSpecifier = spot.score.viewScore.desc();
        else if (j == 1)
            orderSpecifier = spot.score.priceScore.desc();
        else if (j == 2)
            orderSpecifier = spot.score.facilityScore.desc();
        else
            orderSpecifier = spot.score.surroundScore.desc();


        // 지역별 추천된 관광지 담기
        List list = new ArrayList<>();

        //list.add(Location.Aewol_eup);

        log.info("location = {}", locationList);
        for (Location location : locationList) {

            List<RouteSpotListDto> spotListDtos = queryFactory
                    .select(new QRouteSpotListDto(
                                    spot.id,
                                    spot.name,
                                    spot.address,
                                    spot.description,
                                    JPAExpressions
                                            .select(picture.url.max())//스칼라 서브커리에서 limit 못 사용함 그래서 max 사용
                                            .from(picture)
                                            .where(picture.spot.id.eq(spot.id)),
                                    //spot.pictures.any().url//패이징할꺼라 일대다 패치조인 안할거임
                                    //picture.url
                                    spot.location
                            )
                    )
                    .from(spot)
                    .where(locationEq(location))
                    .orderBy(orderSpecifier)
                    .offset(0)
                    .limit(10)
                    .fetch();

            list.add(spotListDtos);

        }

        return list;


    }


    /**
     * 위시리스트 삭제시 연관된 favoriteSpot 삭제해야함 - 벌크 연산
     *
     * @param favoriteId
     */
    @Transactional
    public void deleteFavoriteSpotByFavoriteId(Long favoriteId) {

        queryFactory
                .delete(favoriteSpot)
                .where(favoriteIdEq(favoriteId))
                .execute();

    }


    private BooleanExpression favoriteIdEq(Long favoriteId) {
        return isEmpty(favoriteId) ? null : favoriteSpot.favorite.id.eq(favoriteId);
    }


    private BooleanExpression locationEq(Location location) {
        return location != null ? spot.location.eq(location) : null;
    }

}






