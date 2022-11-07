package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Picture;
import capstone.jejuTourrecommend.spot.domain.detailSpot.repository.PictureRepository;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.PictureDetailDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import capstone.jejuTourrecommend.wishList.domain.dto.PictureUrlDto;
import capstone.jejuTourrecommend.wishList.domain.dto.RouteSpotListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PictureRepositoryImpl implements PictureRepository {

	private final PictureJpaRepository pictureJpaRepository;
	private final PictureQuerydslRepository pictureQuerydslRepository;

	@Override
	public List<Picture> findBySpot(Spot spot) {
		return pictureJpaRepository.findBySpot(spot);
	}

	@Override
	public List<Picture> findAll() {
		return pictureJpaRepository.findAll();
	}

	@Override
	public List<PictureDetailDto> getPictureDetailDtoBySpotIdList(List<Long> spotIdList) {
		return pictureQuerydslRepository.getPictureDetailDtoBySpotIdList(spotIdList);
	}

	@Override
	public List<PictureUrlDto> findPictureUrlDtos(List<Long> spotIdList, Integer limit) {
		return pictureQuerydslRepository.findPictureUrlDtos(spotIdList, limit);
	}
}
