package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.domain.Picture;
import capstone.jejuTourrecommend.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureJpaRepository extends JpaRepository<Picture, Long> {

    List<Picture> findBySpot(Spot spot);

    List<Picture>  findAll();


}
