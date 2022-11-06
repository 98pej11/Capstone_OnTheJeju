package capstone.jejuTourrecommend.spot.domain.detailSpot.repository;

import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Review;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepository {

	List<Review> findBySpot(Spot spot);

	List<Review> findBySpotId(Long spotId);

	Page<ReviewDto> searchSpotReview(Spot spot, Pageable pageable);

}
