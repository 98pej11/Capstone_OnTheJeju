package capstone.jejuTourrecommend.spot.domain.mainSpot.service;

import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.UserWeightDto;
import capstone.jejuTourrecommend.spot.presentation.response.SpotListResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SpotListCommandUseCase {

	SpotListResponse getSpotListWithPriority(Pageable pageable, List locationList, Long memberId,
                                             UserWeightDto userWeightDto);

}
