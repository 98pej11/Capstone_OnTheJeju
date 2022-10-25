package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import java.util.List;

import org.springframework.stereotype.Repository;

import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Picture;
import capstone.jejuTourrecommend.spot.domain.detailSpot.repository.PictureRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PictureRepositoryImpl implements PictureRepository {

	private final PictureJpaRepository pictureJpaRepository;

	@Override
	public List<Picture> findBySpot(Spot spot) {
		return pictureJpaRepository.findBySpot(spot);
	}

	@Override
	public List<Picture> findAll() {
		return pictureJpaRepository.findAll();
	}
}
