package capstone.jejuTourrecommend.spot.domain.mainSpot.service;

import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.UserWeightDto;
import capstone.jejuTourrecommend.spot.presentation.response.ResultSpotListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SpotListCommandUseCase {

    ResultSpotListDto getSpotListWithPriority(Pageable pageable, List locationList, Long memberId, UserWeightDto userWeightDto);

}
