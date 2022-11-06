package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.wishList.domain.dto.ScoreSumDto;

import java.util.List;

public interface ScoreQuerydslRepository {

	ScoreSumDto getScoreSumDto(List<Long> spotIdList);

}
