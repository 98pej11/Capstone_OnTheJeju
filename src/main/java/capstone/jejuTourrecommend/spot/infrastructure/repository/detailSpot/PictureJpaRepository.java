package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.spot.domain.detailSpot.Picture;
import capstone.jejuTourrecommend.spot.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureJpaRepository extends JpaRepository<Picture, Long> {

    List<Picture> findBySpot(Spot spot);

    List<Picture>  findAll();


}
