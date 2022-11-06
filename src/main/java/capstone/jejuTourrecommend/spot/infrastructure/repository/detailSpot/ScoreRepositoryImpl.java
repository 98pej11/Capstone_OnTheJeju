package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ScoreDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScoreRepositoryImpl implements ScoreRepository {

	private final ScoreJpaRepository scoreJpaRepository;

	@Override
	public ScoreDto findScoreBySpotId(Long spotId) {
		return scoreJpaRepository.findScoreBySpotId(spotId);
	}
}
