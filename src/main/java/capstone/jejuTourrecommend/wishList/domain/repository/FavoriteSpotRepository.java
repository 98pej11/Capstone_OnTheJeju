package capstone.jejuTourrecommend.wishList.domain.repository;

import capstone.jejuTourrecommend.wishList.domain.FavoriteSpot;

import java.util.List;
import java.util.Optional;

public interface FavoriteSpotRepository {

	List<FavoriteSpot> findByFavoriteId(Long favoriteId);

	Optional<FavoriteSpot> findOptionBySpotIdAndFavoriteId(Long spotId, Long favoriteId);

	void deleteByFavoriteIdAndSpotId(Long favoriteId, Long spotId);

	void deleteAllByFavoriteId(Long favoriteId);

	void save(FavoriteSpot favoriteSpot);

	List<Long> getSpotIdByFavoriteSpot(Long memberId, List<Long> spotIdList);

	List<Long> findSpotIdByFavoriteId(Long favoriteId);

	List<Long> getSpotIdList(Long favoriteId, List<Long> spotIdList);

}
