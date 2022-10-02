package capstone.jejuTourrecommendV1.repository;


import capstone.jejuTourrecommendV1.domain.Spot;
import capstone.jejuTourrecommendV1.web.pageDto.spotPage.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {

    Page<ReviewDto> searchSpotReview(Spot spot, Pageable pageable);

}
