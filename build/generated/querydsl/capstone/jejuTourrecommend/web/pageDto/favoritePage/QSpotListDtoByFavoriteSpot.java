package capstone.jejuTourrecommend.web.pageDto.favoritePage;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * capstone.jejuTourrecommend.web.pageDto.favoritePage.QSpotListDtoByFavoriteSpot is a Querydsl Projection type for SpotListDtoByFavoriteSpot
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QSpotListDtoByFavoriteSpot extends ConstructorExpression<SpotListDtoByFavoriteSpot> {

    private static final long serialVersionUID = -1667856844L;

    public QSpotListDtoByFavoriteSpot(com.querydsl.core.types.Expression<Long> spotId, com.querydsl.core.types.Expression<String> spotName, com.querydsl.core.types.Expression<String> spotAddress, com.querydsl.core.types.Expression<String> spotDescription, com.querydsl.core.types.Expression<String> url) {
        super(SpotListDtoByFavoriteSpot.class, new Class<?>[]{long.class, String.class, String.class, String.class, String.class}, spotId, spotName, spotAddress, spotDescription, url);
    }

}

