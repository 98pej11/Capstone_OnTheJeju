package capstone.jejuTourrecommend.spot.application;

import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.spot.domain.mainSpot.Category;
import capstone.jejuTourrecommend.spot.domain.mainSpot.Location;
import capstone.jejuTourrecommend.spot.domain.mainSpot.LocationGroup;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.UserWeightDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.service.SpotListService;
import capstone.jejuTourrecommend.spot.presentation.request.MainPageForm;
import capstone.jejuTourrecommend.spot.presentation.response.ResultSpotListDto;
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

	public ResultSpotListDto searchSpotListContains(Long memberId, String spotName, Pageable pageable) {
		return spotListService.searchSpotListContains(memberId, spotName, pageable);
	}

	public ResultSpotListDto getUserSpotList(MainPageForm mainPageForm, Long memberId, Pageable pageable) {
		List<Location> locationList = findLocation(mainPageForm);
		Category category = findCategory(mainPageForm);
		//가중치 존재 유무
		return isPriorityExist(mainPageForm, pageable, locationList, category, memberId);
	}

	private ResultSpotListDto isPriorityExist(MainPageForm mainPageForm, Pageable pageable, List<Location> locationList,Category category, Long memberId) {
		double viewWeight = mainPageForm.getUserWeight().get("viewWeight");
		double priceWeight = mainPageForm.getUserWeight().get("priceWeight");
		double facilityWeight = mainPageForm.getUserWeight().get("facilityWeight");
		double surroundWeight = mainPageForm.getUserWeight().get("surroundWeight");
		double sum = viewWeight + priceWeight + facilityWeight + surroundWeight;
		if (sum == 0) {
			return spotListService.getSpotListWithoutPriority(pageable, locationList, category, memberId);
		}
		//사용자가 가중치를 넣은 경우 //readOnly X
		UserWeightDto userWeightDto = new UserWeightDto(viewWeight, priceWeight, facilityWeight, surroundWeight);
		return spotListService.getSpotListWithPriority(pageable, locationList, memberId, userWeightDto);
	}

	public Category findCategory(MainPageForm mainPageForm) {
		return Category.fromName(mainPageForm.getCategory());
	}

	public List<Location> findLocation(MainPageForm mainPageForm) {
		if (!StringUtils.hasText(mainPageForm.getLocation())) {
			throw new UserException("지역에 null 값이 들어갔습니다");
		}
		return LocationGroup.getLocations(mainPageForm.getLocation());
	}

}
