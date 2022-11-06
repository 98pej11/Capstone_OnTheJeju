package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ScoreDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.repository.ScoreRepository;
import capstone.jejuTourrecommend.wishList.domain.dto.ScoreSumDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScoreRepositoryImpl implements ScoreRepository {

	private final ScoreJpaRepository scoreJpaRepository;
	private final ScoreQuerydslRepository scoreQuerydslRepository;

	@Override
	public ScoreDto findScoreBySpotId(Long spotId) {
		return scoreJpaRepository.findScoreBySpotId(spotId);
	}

	@Override
	public ScoreSumDto getScoreSumDto(List<Long> spotIdList) {
		return scoreQuerydslRepository.getScoreSumDto(spotIdList);
	}
}
