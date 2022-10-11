package capstone.jejuTourrecommend.spot.domain.mainSpot.service;

import capstone.jejuTourrecommend.spot.domain.mainSpot.Category;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.UserWeightDto;
import capstone.jejuTourrecommend.spot.presentation.response.ResultSpotListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SpotListQueryUserCase {

    ResultSpotListDto searchSpotListContains(Long memberId, String spotName, Pageable pageable);

    ResultSpotListDto getSpotListWithoutPriority(Pageable pageable, List locationList, Category category, Long memberId);

}
