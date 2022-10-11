package capstone.jejuTourrecommend.wishList.domain.repository;

import capstone.jejuTourrecommend.wishList.domain.FavoriteSpot;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import capstone.jejuTourrecommend.wishList.domain.dto.SpotListDtoByFavoriteSpot;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.RouteForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FavoriteSpotRepository {

    List<FavoriteSpot> findByFavoriteId(Long favoriteId);

    Optional<FavoriteSpot> findOptionBySpotIdAndFavoriteId(Long spotId, Long favoriteId);

    void deleteByFavoriteIdAndSpotId(Long favoriteId, Long spotId);

    Page<FavoriteListDto> getFavoriteList(Long memberId, Pageable pageable);

    List<SpotListDtoByFavoriteSpot> favoriteSpotList(Long favoriteId);

    FavoriteSpot existSpot(Long favoriteId, RouteForm routeForm);

    List recommendSpotList(Long favoriteId, RouteForm routeForm);

    void deleteFavoriteSpotByFavoriteId(Long favoriteId);

    void save(FavoriteSpot favoriteSpot);
}
