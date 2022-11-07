package capstone.jejuTourrecommend.wishList.infrastructure.repository;

import capstone.jejuTourrecommend.wishList.domain.FavoriteSpot;
import capstone.jejuTourrecommend.wishList.domain.repository.FavoriteSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FavoriteSpotRepositoryImpl implements FavoriteSpotRepository {

	private final FavoriteSpotJpaRepository favoriteSpotJpaRepository;
	private final FavoriteSpotQuerydslRepository favoriteSpotQuerydslRepository;

	@Override
	public List<FavoriteSpot> findByFavoriteId(Long favoriteId) {
		return favoriteSpotJpaRepository.findByFavoriteId(favoriteId);
	}

	@Override
	public Optional<FavoriteSpot> findOptionBySpotIdAndFavoriteId(Long spotId, Long favoriteId) {
		return favoriteSpotJpaRepository.findOptionBySpotIdAndFavoriteId(spotId, favoriteId);
	}

	@Override
	public void deleteByFavoriteIdAndSpotId(Long favoriteId, Long spotId) {
		favoriteSpotJpaRepository.deleteByFavoriteIdAndSpotId(favoriteId, spotId);
	}

	@Override
	public void save(FavoriteSpot favoriteSpot) {
		favoriteSpotJpaRepository.save(favoriteSpot);
	}

	@Override
	public List<Long> getSpotIdByFavoriteSpot(Long memberId, List<Long> spotIdList) {
		return favoriteSpotQuerydslRepository.getSpotIdByFavoriteSpot(memberId, spotIdList);
	}

	@Override
	public List<Long> findSpotIdByFavoriteId(Long favoriteId) {
		return favoriteSpotJpaRepository.findSpotIdByFavoriteId(favoriteId);
	}

	@Override
	public List<Long> getSpotIdList(Long favoriteId, List<Long> spotIdList) {
		return favoriteSpotQuerydslRepository.getSpotIdList(favoriteId, spotIdList);
	}

	@Override
	public void deleteAllByFavoriteId(Long favoriteId) {
		favoriteSpotJpaRepository.deleteAllByFavoriteId(favoriteId);
	}

}
