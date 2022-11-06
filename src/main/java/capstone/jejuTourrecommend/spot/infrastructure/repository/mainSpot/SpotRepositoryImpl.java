package capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot;

import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.mainSpot.Category;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class SpotRepositoryImpl implements SpotRepository {

	private final SpotJpaRepository spotJpaRepository;

	private final SpotQuerydslRepository spotQuerydslRepository;

	@Override
	public Optional<Spot> findOptionByName(String spotName) {
		return spotJpaRepository.findOptionByName(spotName);
	}

	@Override
	public Optional<Spot> findOptionById(Long spotId) {
		return spotJpaRepository.findOptionById(spotId);
	}

	@Override
	public List<Spot> findByNameLike(String spotName) {
		return spotJpaRepository.findByNameLike(spotName);
	}


	@Override
	public Page<SpotListDto> searchBySpotNameContains(Long memberId, String spotName, Pageable pageable) {
		return spotQuerydslRepository.searchBySpotNameContains(memberId, spotName, pageable);
	}

	@Override
	public Page<SpotListDto> searchSpotByLocationAndCategory(Long memberId, List locationList, Category category,
		Pageable pageable) {
		return spotQuerydslRepository.searchSpotByLocationAndCategory(memberId, locationList, category, pageable);
	}

	@Override
	public List<Spot> findSpotFetchJoinBySpotIdList(List<Long> spotIdList) {
		return spotJpaRepository.findSpotFetchJoinBySpotIdList(spotIdList);
	}
}
