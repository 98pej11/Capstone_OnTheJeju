package capstone.jejuTourrecommend.web.pageDto.routePage;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * capstone.jejuTourrecommend.web.pageDto.routePage.QRouteSpotListDto is a Querydsl Projection type for RouteSpotListDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QRouteSpotListDto extends ConstructorExpression<RouteSpotListDto> {

    private static final long serialVersionUID = 2040070383L;

    public QRouteSpotListDto(com.querydsl.core.types.Expression<Long> spotId, com.querydsl.core.types.Expression<String> spotName, com.querydsl.core.types.Expression<String> spotAddress, com.querydsl.core.types.Expression<String> spotDescription, com.querydsl.core.types.Expression<String> url, com.querydsl.core.types.Expression<capstone.jejuTourrecommend.domain.Location> location) {
        super(RouteSpotListDto.class, new Class<?>[]{long.class, String.class, String.class, String.class, String.class, capstone.jejuTourrecommend.domain.Location.class}, spotId, spotName, spotAddress, spotDescription, url, location);
    }

}

