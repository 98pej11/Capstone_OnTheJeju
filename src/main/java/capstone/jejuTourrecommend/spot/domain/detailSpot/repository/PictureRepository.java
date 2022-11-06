package capstone.jejuTourrecommend.spot.domain.detailSpot.repository;

import java.util.List;

import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Picture;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.PictureDetailDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;

public interface PictureRepository {

	List<Picture> findBySpot(Spot spot);

	List<Picture> findAll();

	List<PictureDetailDto> postSpotPictureUrlsToDto(List<SpotListDto> spotListDtoList);
}
