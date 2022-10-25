package capstone.jejuTourrecommend.spot.domain.mainSpot.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import capstone.jejuTourrecommend.spot.domain.mainSpot.Category;
import capstone.jejuTourrecommend.spot.presentation.response.ResultSpotListDto;

public interface SpotListQueryUserCase {

	ResultSpotListDto searchSpotListContains(Long memberId, String spotName, Pageable pageable);

	ResultSpotListDto getSpotListWithoutPriority(Pageable pageable, List locationList, Category category,
		Long memberId);

}
