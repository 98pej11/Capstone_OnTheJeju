package capstone.jejuTourrecommend.spot.domain.detailSpot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ReviewDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.SpotDetailDto;

public interface DetailSpotQueryUseCase {

	Page<ReviewDto> reviewPage(Long spotId, Pageable pageable);

	SpotDetailDto spotPage(Long spotId, Long memberId);

}
