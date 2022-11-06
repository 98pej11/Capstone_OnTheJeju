package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.PictureDetailDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import capstone.jejuTourrecommend.wishList.domain.dto.PictureUrlDto;

import java.util.List;

public interface PictureQuerydslRepository {

	List<PictureDetailDto> getPictureDetailDtoBySpotIdList(List<SpotListDto> spotListDtoList);

	List<PictureUrlDto> findPictureUrlDtos(List<Long> spotIdList,Integer limit);

}
