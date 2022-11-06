package capstone.jejuTourrecommend.wishList.infrastructure.repository;

import capstone.jejuTourrecommend.wishList.presentation.dto.request.RouteForm;

import java.util.List;

public interface FavoriteSpotQuerydslRepository {

	List<Long> getBooleanFavoriteSpot(Long memberId, List<Long> spotIdList);

	List recommendSpotList(Long favoriteId, RouteForm routeForm);

}
