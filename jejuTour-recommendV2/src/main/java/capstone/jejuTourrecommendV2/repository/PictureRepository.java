package capstone.jejuTourrecommendV2.repository;

import capstone.jejuTourrecommendV2.domain.Picture;
import capstone.jejuTourrecommendV2.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, Long> {

    List<Picture> findBySpot(Spot spot);

    List<Picture>  findAll();


}
