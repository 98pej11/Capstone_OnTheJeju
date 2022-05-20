package capstone.jejuTourrecommend.repository;

import capstone.jejuTourrecommend.domain.FavoriteSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteSpotRepository extends JpaRepository<FavoriteSpot, Long> {

    List<FavoriteSpot> findByFavoriteId(Long favoriteId);

    Optional<FavoriteSpot> findOptionBySpotIdAndFavoriteId(Long spotId, Long favoriteId);


}













