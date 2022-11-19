package capstone.jejuTourrecommend.spot.application;

import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.spot.domain.mainSpot.Category;
import capstone.jejuTourrecommend.spot.domain.mainSpot.Location;
import capstone.jejuTourrecommend.spot.domain.mainSpot.LocationGroup;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.UserWeightDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.service.SpotListService;
import capstone.jejuTourrecommend.spot.presentation.request.MainPageRequest;
import capstone.jejuTourrecommend.spot.presentation.response.SpotListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotListFacade {

	private final SpotListService spotListService;

	public SpotListResponse searchSpotListContains(Long memberId, String spotName, Pageable pageable) {
		return spotListService.searchSpotListContains(memberId, spotName, pageable);
	}

	public SpotListResponse getUserSpotList(MainPageRequest mainPageRequest, Long memberId, Pageable pageable) {
		List<Location> locationList = findLocation(mainPageRequest);
		Category category = findCategory(mainPageRequest);
		//가중치 존재 유무
		return isPriorityExist(mainPageRequest, pageable, locationList, category, memberId);
	}

	private SpotListResponse isPriorityExist(MainPageRequest mainPageRequest, Pageable pageable, List<Location> locationList, Category category, Long memberId) {
		double viewWeight = mainPageRequest.getUserWeight().get("viewWeight");
		double priceWeight = mainPageRequest.getUserWeight().get("priceWeight");
		double facilityWeight = mainPageRequest.getUserWeight().get("facilityWeight");
		double surroundWeight = mainPageRequest.getUserWeight().get("surroundWeight");
		double sum = viewWeight + priceWeight + facilityWeight + surroundWeight;
		if (sum == 0) {
			return spotListService.getSpotListWithoutPriority(pageable, locationList, category, memberId);
		}
		//사용자가 가중치를 넣은 경우 //readOnly X
		UserWeightDto userWeightDto = new UserWeightDto(viewWeight, priceWeight, facilityWeight, surroundWeight);
		return spotListService.getSpotListWithPriority(pageable, locationList, memberId, userWeightDto);
	}

	public Category findCategory(MainPageRequest mainPageRequest) {
		return Category.fromName(mainPageRequest.getCategory());
	}

	public List<Location> findLocation(MainPageRequest mainPageRequest) {
		if (!StringUtils.hasText(mainPageRequest.getLocation())) {
			throw new UserException("지역에 null 값이 들어갔습니다");
		}
		return LocationGroup.getLocations(mainPageRequest.getLocation());
	}

}
