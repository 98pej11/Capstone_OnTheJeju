package capstone.jejuTourrecommend.web.pageDto.mainPage;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * capstone.jejuTourrecommend.web.pageDto.mainPage.QSpotScoreDto is a Querydsl Projection type for SpotScoreDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QSpotScoreDto extends ConstructorExpression<SpotScoreDto> {

    private static final long serialVersionUID = 1525420146L;

    public QSpotScoreDto(com.querydsl.core.types.Expression<Double> viewScore, com.querydsl.core.types.Expression<Double> priceScore, com.querydsl.core.types.Expression<Double> facilityScore, com.querydsl.core.types.Expression<Double> surroundScore) {
        super(SpotScoreDto.class, new Class<?>[]{double.class, double.class, double.class, double.class}, viewScore, priceScore, facilityScore, surroundScore);
    }

}

