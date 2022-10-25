package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Picture;

public interface PictureJpaRepository extends JpaRepository<Picture, Long> {

	List<Picture> findBySpot(Spot spot);

	List<Picture> findAll();

}
