package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.wishList.domain.dto.ScoreSumDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static capstone.jejuTourrecommend.spot.domain.QScore.score;
import static capstone.jejuTourrecommend.spot.domain.QSpot.spot;

@Repository
@Slf4j
@Transactional
public class ScoreQuerydslRepositoryImpl implements ScoreQuerydslRepository{

	private final JPAQueryFactory queryFactory;

	public ScoreQuerydslRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	public ScoreSumDto getScoreSumDto(List<Long> spotIdList) {

		List<ScoreSumDto> scoreSumDtos = queryFactory
			.select(
				Projections.constructor(
					ScoreSumDto.class,
					spot.score.viewScore.sum(),
					spot.score.priceScore.sum(),
					spot.score.facilityScore.sum(),
					spot.score.surroundScore.sum()
				)
			)
			.from(spot)
			.innerJoin(spot.score, score)
			.where(spot.id.in(spotIdList))
			.fetch();

		return scoreSumDtos.get(0);
	}

}
