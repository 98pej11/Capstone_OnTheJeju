package capstone.jejuTourrecommend.spot.domain.mainSpot.service;

import capstone.jejuTourrecommend.spot.domain.mainSpot.Category;
import capstone.jejuTourrecommend.spot.presentation.response.SpotListResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SpotListQueryUserCase {

	SpotListResponse searchSpotListContains(Long memberId, String spotName, Pageable pageable);

	SpotListResponse getSpotListWithoutPriority(Pageable pageable, List locationList, Category category,
                                                Long memberId);

}
