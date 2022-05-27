package capstone.jejuTourrecommend.repository;


import capstone.jejuTourrecommend.domain.*;
import capstone.jejuTourrecommend.web.login.exceptionClass.UserException;
import capstone.jejuTourrecommend.web.mainPage.QSpotListDto;
import capstone.jejuTourrecommend.web.pageDto.mainPage.SpotListDto;
import capstone.jejuTourrecommend.web.pageDto.routePage.RouteForm;
import capstone.jejuTourrecommend.web.routePage.QRouteSpotListDto;
import capstone.jejuTourrecommend.web.pageDto.routePage.RouteSpotListDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static capstone.jejuTourrecommend.domain.QFavoriteSpot.favoriteSpot;
import static capstone.jejuTourrecommend.domain.QPicture.picture;
import static capstone.jejuTourrecommend.domain.QSpot.spot;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
@Slf4j
public class FavoriteSpotQueryRepository {

    private final JPAQueryFactory queryFactory;

    public FavoriteSpotQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Transactional
    public List<SpotListDto> favoriteSpotList(Long favoriteId){
        List<SpotListDto> spotListDtoList = queryFactory
                .select(
                        new QSpotListDto(
                                spot.id,
                                spot.name,
                                spot.address,
                                spot.description,
                                JPAExpressions
                                        .select(picture.url.max())//스칼라 서브커리에서 limit 못 사용함 그래서 max 사용
                                        .from(picture)
                                        .where(picture.spot.id.eq(favoriteSpot.spot.id))
                        )
                )
                .from(favoriteSpot)
                .join(favoriteSpot.spot,spot)//명시적 조인
                //.join(favoriteSpot.spot, spot).fetchJoin()
                .where(favoriteSpot.favorite.id.eq(favoriteId))
                .fetch();



        return spotListDtoList;

    }

    @Transactional
    public FavoriteSpot existSpot(Long favoriteId, RouteForm routeForm){
        FavoriteSpot favoriteSpot = queryFactory
                .selectFrom(QFavoriteSpot.favoriteSpot)
                .where(favoriteIdEq(favoriteId), spot.id.in(routeForm.getSpotIdList()))
                .fetchOne();

        return favoriteSpot;

    }

    @Transactional
    public List recommendSpotList(Long favoriteId, RouteForm routeForm){

        List<Spot> spotList = queryFactory
                .select(spot)
                .from(favoriteSpot)
                .join(favoriteSpot.spot,spot)//명시적 조인// 근데 여기서는 알아서 최적화를 해줌
                .where(favoriteIdEq(favoriteId),spot.id.in(routeForm.getSpotIdList()))
                .fetch();

        if(isEmpty(spotList)){
            log.info("spotList = {}",spotList);
            throw new UserException("모든 spotId가 위시리스트에 있는 spotId가 아닙니다");
        }

        List<Tuple> tupleList = queryFactory
                .select(
                        spot.score.viewScore.sum(),
                        spot.score.priceScore.sum(),
                        spot.score.facilityScore.sum(),
                        spot.score.surroundScore.sum()
                )
                .from(spot)
                .where(spot.in(spotList))
                .fetch();

        List<Location> locationList = queryFactory
                .select(spot.location).distinct()
                .from(spot)
                .where(spot.in(spotList))
                .fetch();


        //최대값을 가진 카테고리 구하기
        Tuple tuple = tupleList.get(0);
        Double[] score = new Double[4];
        score[0] = tuple.get(spot.score.viewScore.sum());
        log.info("score[0] = {}",score[0]);
        score[1] = tuple.get(spot.score.priceScore.sum());
        score[2] = tuple.get(spot.score.facilityScore.sum());
        score[3] = tuple.get(spot.score.surroundScore.sum());
        Double max =score[0];
        int j=0;

        for(int i =0;i<4;i++){
            if(max<score[i]){
                j=i;
                max = score[i];
            }
        }


        OrderSpecifier<Double> orderSpecifier;
        if(j==0)
            orderSpecifier = spot.score.viewScore.desc();
        else if (j==1)
            orderSpecifier = spot.score.priceScore.desc();
        else if(j==2)
            orderSpecifier = spot.score.facilityScore.desc();
        else
            orderSpecifier = spot.score.surroundScore.desc();


        List list= new ArrayList<>();

        //list.add(Location.Aewol_eup);

        log.info("location = {}",locationList);
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



    @Transactional
    public void deleteFavoriteSpotByFavoriteId(Long favoriteId){

        queryFactory
                .delete(favoriteSpot)
                .where(favoriteIdEq(favoriteId))
                .execute();

    }

//    @Transactional
//    public void deleteFavoriteSpotByFavoriteIdAndSpotId(Long favoriteId, Long spotId){
//
//        queryFactory
//                .delete(favoriteSpot)
//                .where(favoriteIdEq(favoriteId),spotIdEq(spotId))
//                .execute();
//    }

    //이거 없어도 될것같음 그냥 특정 favorite하고 연관된것만 지우면 됨
    private BooleanExpression spotIdEq(Long spotId){
        return isEmpty(spotId) ? null : favoriteSpot.spot.id.eq(spotId);
    }

    private BooleanExpression favoriteIdEq(Long favoriteId){
        return isEmpty(favoriteId) ? null : favoriteSpot.favorite.id.eq(favoriteId);
    }

    private BooleanExpression locationEq(Location location) {
        return location != null ? spot.location.eq(location) : null;
    }

}






