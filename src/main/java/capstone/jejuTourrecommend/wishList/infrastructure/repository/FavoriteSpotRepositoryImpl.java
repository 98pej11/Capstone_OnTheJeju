package capstone.jejuTourrecommend.wishList.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import capstone.jejuTourrecommend.wishList.domain.FavoriteSpot;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import capstone.jejuTourrecommend.wishList.domain.dto.SpotListDtoByFavoriteSpot;
import capstone.jejuTourrecommend.wishList.domain.repository.FavoriteSpotRepository;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.RouteForm;
import lombok.RequiredArgsConstructor;

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
	public List<Long> getBooleanFavoriteSpot(Long memberId, List<Long> spotIdList) {
		return favoriteSpotQuerydslRepository.getBooleanFavoriteSpot(memberId, spotIdList);
	}

	//////////////
	@Override
	public void deleteAllByFavoriteId(Long favoriteId) {
		favoriteSpotJpaRepository.deleteAllByFavoriteId(favoriteId);
	}

	@Override
	public Page<FavoriteListDto> getFavoriteList(Long memberId, Pageable pageable) {
		return favoriteSpotQuerydslRepository.getFavoriteList(memberId, pageable);
	}

	@Override
	public List<SpotListDtoByFavoriteSpot> favoriteSpotList(Long favoriteId) {
		return favoriteSpotQuerydslRepository.favoriteSpotList(favoriteId);
	}

	@Override
	public FavoriteSpot existSpot(Long favoriteId, RouteForm routeForm) {
		return favoriteSpotQuerydslRepository.existSpot(favoriteId, routeForm);
	}

	@Override
	public List recommendSpotList(Long favoriteId, RouteForm routeForm) {
		return favoriteSpotQuerydslRepository.recommendSpotList(favoriteId, routeForm);
	}

}
