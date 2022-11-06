package capstone.jejuTourrecommend.spot.domain.detailSpot.repository;

import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ScoreDto;

public interface ScoreRepository {

	ScoreDto findScoreBySpotId(Long spotId);
}
