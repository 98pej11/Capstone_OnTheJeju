package capstone.jejuTourrecommend.spot.domain.detailSpot.repository;

import java.util.List;

import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Picture;

public interface PictureRepository {

	List<Picture> findBySpot(Spot spot);

	List<Picture> findAll();
}
