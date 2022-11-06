package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.PictureDetailDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;

import java.util.List;

public interface PictureQuerydslRepository {

	List<PictureDetailDto> postSpotPictureUrlsToDto(List<SpotListDto> spotListDtoList);
}
