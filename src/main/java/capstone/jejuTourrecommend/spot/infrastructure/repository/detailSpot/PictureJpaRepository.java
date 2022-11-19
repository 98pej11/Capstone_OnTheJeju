package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PictureJpaRepository extends JpaRepository<Picture, Long> {

	@Query("select p from Picture p where p.spot.id = :spotId")
	List<Picture> findBySpotId(@Param("spotId") Long spotId);

	List<Picture> findAll();

}
