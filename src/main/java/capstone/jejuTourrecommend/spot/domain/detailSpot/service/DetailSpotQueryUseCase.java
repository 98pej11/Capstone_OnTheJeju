package capstone.jejuTourrecommend.spot.domain.detailSpot.service;

import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ReviewDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.SpotDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DetailSpotQueryUseCase {

    Page<ReviewDto> reviewPage(Long spotId, Pageable pageable);

    SpotDetailDto spotPage(Long spotId, Long memberId);

}
