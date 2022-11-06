package capstone.jejuTourrecommend.wishList.infrastructure.repository;

import capstone.jejuTourrecommend.wishList.domain.FavoriteSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteSpotJpaRepository extends JpaRepository<FavoriteSpot, Long> {

	List<FavoriteSpot> findByFavoriteId(Long favoriteId);

	Optional<FavoriteSpot> findOptionBySpotIdAndFavoriteId(Long spotId, Long favoriteId);

	@Modifying(clearAutomatically = true)
	@Query("delete from FavoriteSpot fs where fs.favorite.id= :favoriteId and fs.spot.id= :spotId")
	void deleteByFavoriteIdAndSpotId(@Param("favoriteId") Long favoriteId, @Param("spotId") Long spotId);

	@Modifying(clearAutomatically = true)
	@Query("delete from FavoriteSpot fs where fs.favorite.id= :favoriteId")
	void deleteAllByFavoriteId(@Param("favoriteId") Long favoriteId);

	@Query("select fs.spot.id from FavoriteSpot fs where fs.favorite.id = :favoriteId")
	List<Long> findSpotIdByFavoriteId(@Param("favoriteId") Long favoriteId);


}













