package capstone.jejuTourrecommend.wishList.domain.repository;

import capstone.jejuTourrecommend.wishList.domain.FavoriteSpot;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.RouteForm;

import java.util.List;
import java.util.Optional;

public interface FavoriteSpotRepository {

	List<FavoriteSpot> findByFavoriteId(Long favoriteId);

	Optional<FavoriteSpot> findOptionBySpotIdAndFavoriteId(Long spotId, Long favoriteId);

	void deleteByFavoriteIdAndSpotId(Long favoriteId, Long spotId);

	List recommendSpotList(Long favoriteId, RouteForm routeForm);

	void deleteAllByFavoriteId(Long favoriteId);

	void save(FavoriteSpot favoriteSpot);

	List<Long> getBooleanFavoriteSpot(Long memberId, List<Long> spotIdList);

	List<Long> findSpotIdByFavoriteId(Long favoriteId);

}
