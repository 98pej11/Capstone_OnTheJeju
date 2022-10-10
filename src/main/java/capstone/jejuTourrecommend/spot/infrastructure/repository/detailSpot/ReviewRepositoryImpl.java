package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.spot.domain.detailSpot.Review;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ReviewDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;

    private final ReviewQuerydslRepository reviewQuerydslRepository;

    @Override
    public List<Review> findBySpot(Spot spot) {
        return reviewJpaRepository.findBySpot(spot);
    }

    @Override
    public List<Review> findBySpotId(Long spotId) {
        return reviewJpaRepository.findBySpotId(spotId);
    }

    @Override
    public Page<ReviewDto> searchSpotReview(Spot spot, Pageable pageable) {
        return reviewQuerydslRepository.searchSpotReview(spot, pageable);
    }
}
