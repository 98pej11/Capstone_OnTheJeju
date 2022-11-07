package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.PictureDetailDto;
import capstone.jejuTourrecommend.wishList.domain.dto.PictureUrlDto;

import java.util.List;

public interface PictureQuerydslRepository {

	List<PictureDetailDto> getPictureDetailDtoBySpotIdList(List<Long> spotIdList);

	List<PictureUrlDto> findPictureUrlDtos(List<Long> spotIdList,Integer limit);
}
