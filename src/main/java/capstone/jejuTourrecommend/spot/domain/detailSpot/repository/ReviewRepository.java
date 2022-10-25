package capstone.jejuTourrecommend.spot.domain.detailSpot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Review;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ReviewDto;

public interface ReviewRepository {

	List<Review> findBySpot(Spot spot);

	List<Review> findBySpotId(Long spotId);

	Page<ReviewDto> searchSpotReview(Spot spot, Pageable pageable);

}
