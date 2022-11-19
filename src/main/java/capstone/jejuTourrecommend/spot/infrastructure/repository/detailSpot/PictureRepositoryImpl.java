package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Picture;
import capstone.jejuTourrecommend.spot.domain.detailSpot.repository.PictureRepository;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.PictureDetailDto;
import capstone.jejuTourrecommend.wishList.domain.dto.PictureUrlDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PictureRepositoryImpl implements PictureRepository {

	private final PictureJpaRepository pictureJpaRepository;
	private final PictureQuerydslRepository pictureQuerydslRepository;

	@Override
	public List<Picture> findBySpotId(Long spotId) {
		return pictureJpaRepository.findBySpotId(spotId);
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
