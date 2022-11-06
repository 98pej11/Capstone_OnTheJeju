package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import java.util.List;

import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.PictureDetailDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import org.springframework.stereotype.Repository;

import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Picture;
import capstone.jejuTourrecommend.spot.domain.detailSpot.repository.PictureRepository;
import lombok.RequiredArgsConstructor;

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
	public List<PictureDetailDto> postSpotPictureUrlsToDto(List<SpotListDto> spotListDtoList) {
		return pictureQuerydslRepository.postSpotPictureUrlsToDto(spotListDtoList);
	}


}
