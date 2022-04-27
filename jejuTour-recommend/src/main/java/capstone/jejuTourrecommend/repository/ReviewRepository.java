package capstone.jejuTourrecommend.repository;

import capstone.jejuTourrecommend.domain.Review;
import capstone.jejuTourrecommend.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long>, ReviewRepositoryCustom{

    //List<Review> findBySpot(Long spotId); 이거 안됨

    List<Review> findBySpot(Spot spot);

}








