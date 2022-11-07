package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.PictureDetailDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import capstone.jejuTourrecommend.wishList.domain.dto.PictureUrlDto;
import capstone.jejuTourrecommend.wishList.domain.dto.RouteSpotListDto;

import java.util.List;

public interface PictureQuerydslRepository {

	List<PictureDetailDto> getPictureDetailDtoBySpotIdList(List<Long> spotIdList);

	List<PictureUrlDto> findPictureUrlDtos(List<Long> spotIdList,Integer limit);
}
