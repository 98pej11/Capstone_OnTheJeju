package capstone.jejuTourrecommend.spot.domain.detailSpot.repository;

import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Picture;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.PictureDetailDto;
import capstone.jejuTourrecommend.wishList.domain.dto.PictureUrlDto;

import java.util.List;

public interface PictureRepository {

	List<Picture> findBySpotId(Long spotId);

	List<Picture> findAll();

	List<PictureDetailDto> getPictureDetailDtoBySpotIdList(List<Long> spotIdList);

	List<PictureUrlDto> findPictureUrlDtos(List<Long> spotIdList, Integer limit);

}
