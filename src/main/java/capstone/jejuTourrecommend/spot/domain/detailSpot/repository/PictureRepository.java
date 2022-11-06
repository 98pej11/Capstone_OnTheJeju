package capstone.jejuTourrecommend.spot.domain.detailSpot.repository;

import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Picture;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.PictureDetailDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import capstone.jejuTourrecommend.wishList.domain.dto.PictureUrlDto;

import java.util.List;

public interface PictureRepository {

	List<Picture> findBySpot(Spot spot);

	List<Picture> findAll();

	List<PictureDetailDto> getPictureDetailDtoBySpotIdList(List<SpotListDto> spotListDtoList);

	List<PictureUrlDto> findPictureUrlDtos(List<Long> spotIdList, Integer limit);
}
