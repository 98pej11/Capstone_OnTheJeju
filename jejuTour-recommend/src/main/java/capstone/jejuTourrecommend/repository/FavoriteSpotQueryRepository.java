package capstone.jejuTourrecommend.repository;


import capstone.jejuTourrecommend.domain.QFavorite;
import capstone.jejuTourrecommend.domain.QFavoriteSpot;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static capstone.jejuTourrecommend.domain.QFavorite.favorite;
import static capstone.jejuTourrecommend.domain.QFavoriteSpot.favoriteSpot;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
public class FavoriteSpotQueryRepository {

    private final JPAQueryFactory queryFactory;

    public FavoriteSpotQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Transactional
    public void deleteFavoriteSpot(Long favoriteId){

        queryFactory
                .delete(favoriteSpot)
                .where(favoriteIdEq(favoriteId))
                .execute();

//        queryFactory
//                .delete(favorite)
//                .where(favoriteIdEq(favoriteId))
//                .execute();



    }

    //이거 없어도 될것같음 그냥 특정 favorite하고 연관된것만 지우면 됨
    private BooleanExpression spotIdEq(Long spotId){
        return isEmpty(spotId) ? null : favoriteSpot.spot.id.eq(spotId);
    }
    private BooleanExpression favoriteIdEq(Long favoriteId){
        return isEmpty(favoriteId) ? null : favoriteSpot.favorite.id.eq(favoriteId);
    }

}










