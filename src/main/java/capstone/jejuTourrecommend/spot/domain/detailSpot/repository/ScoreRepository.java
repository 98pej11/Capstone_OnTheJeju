package capstone.jejuTourrecommend.spot.domain.detailSpot.repository;

import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ScoreDto;
import capstone.jejuTourrecommend.wishList.domain.dto.ScoreSumDto;

import java.util.List;

public interface ScoreRepository {

	ScoreDto findScoreBySpotId(Long spotId);

	ScoreSumDto getScoreSumDto(List<Long> spotIdList);
}
