package capstone.jejuTourrecommendV1.repository;

import capstone.jejuTourrecommendV1.domain.Picture;
import capstone.jejuTourrecommendV1.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, Long> {

    List<Picture> findBySpot(Spot spot);

    List<Picture>  findAll();


}
