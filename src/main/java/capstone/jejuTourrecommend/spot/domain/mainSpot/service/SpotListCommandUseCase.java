package capstone.jejuTourrecommend.spot.domain.mainSpot.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.UserWeightDto;
import capstone.jejuTourrecommend.spot.presentation.response.ResultSpotListDto;

public interface SpotListCommandUseCase {

	ResultSpotListDto getSpotListWithPriority(Pageable pageable, List locationList, Long memberId,
		UserWeightDto userWeightDto);

}
