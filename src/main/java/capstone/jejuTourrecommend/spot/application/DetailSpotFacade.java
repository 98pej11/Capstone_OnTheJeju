package capstone.jejuTourrecommend.spot.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ReviewDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.SpotDetailDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.service.DetailSpotQueryUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetailSpotFacade {

	private final DetailSpotQueryUseCase detailSpotQueryUseCase;

	public Page<ReviewDto> reviewPage(Long spotId, Pageable pageable) {
		return detailSpotQueryUseCase.reviewPage(spotId, pageable);
	}

	public SpotDetailDto spotPage(Long spotId, Long memberId) {
		return detailSpotQueryUseCase.spotPage(spotId, memberId);
	}

}
