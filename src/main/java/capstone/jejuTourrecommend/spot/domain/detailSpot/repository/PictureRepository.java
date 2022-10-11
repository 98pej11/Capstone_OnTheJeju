package capstone.jejuTourrecommend.spot.domain.detailSpot.repository;

import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Picture;

import java.util.List;

public interface PictureRepository {

    List<Picture> findBySpot(Spot spot);

    List<Picture>  findAll();
}
