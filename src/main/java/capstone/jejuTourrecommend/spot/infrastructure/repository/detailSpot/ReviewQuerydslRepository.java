package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;


import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewQuerydslRepository {

    Page<ReviewDto> searchSpotReview(Spot spot, Pageable pageable);

}
