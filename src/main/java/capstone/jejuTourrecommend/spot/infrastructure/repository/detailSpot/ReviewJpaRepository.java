package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

	//List<Review> findBySpot(Long spotId); 이거 안됨

	List<Review> findBySpot(Spot spot);

	List<Review> findBySpotId(Long spotId);//여기서 페이징으로 반환하여 가능하기는 한데 count 셀때 다로 쿼리 날려야함

}








