package capstone.jejuTourrecommend.repository;

import capstone.jejuTourrecommend.domain.Location;
import capstone.jejuTourrecommend.domain.QPicture;
import capstone.jejuTourrecommend.domain.QSpot;
import capstone.jejuTourrecommend.domain.Spot;
import capstone.jejuTourrecommend.web.mainPage.QSpotLocationDto;
import capstone.jejuTourrecommend.web.mainPage.SpotLocationDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.List;

import static capstone.jejuTourrecommend.domain.QPicture.*;
import static capstone.jejuTourrecommend.domain.QSpot.*;

public class SpotRepositoryImpl implements SpotRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public SpotRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<SpotLocationDto> findSpotByLocation(Location location) {


        List<SpotLocationDto> spotLocationDtoList = queryFactory
                .select(new QSpotLocationDto(
                        spot.id,
                        spot.name,
                        spot.address,
                        spot.description,
                        JPAExpressions
                                .select(picture.url)
                                .from(picture)
                                .where(picture.spot.id.eq(spot.id))
                        //spot.pictures.any().url//패이징할꺼라 일대다 패치조인 안할거임
                        //picture.url
                ))
                .from(spot)
                //.leftJoin(picture.spot, spot).fetchJoin()
                .where(spot.location.eq(location))
                .orderBy(spot.score.rankAverage.desc())
                .fetch();

        for (SpotLocationDto spotLocationDto : spotLocationDtoList) {
            System.out.println("spotLocationDto.getUrl() = " + spotLocationDto.getUrl());
        }


         return spotLocationDtoList;
    }
}














