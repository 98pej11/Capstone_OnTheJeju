package capstone.jejuTourrecommendV2.repository;


import capstone.jejuTourrecommendV2.domain.Spot;
import capstone.jejuTourrecommendV2.web.pageDto.spotPage.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {

    Page<ReviewDto> searchSpotReview(Spot spot, Pageable pageable);

}
