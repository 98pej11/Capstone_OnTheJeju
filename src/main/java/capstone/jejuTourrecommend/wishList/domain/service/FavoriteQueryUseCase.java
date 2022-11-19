package capstone.jejuTourrecommend.wishList.domain.service;

import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import capstone.jejuTourrecommend.wishList.domain.dto.RouteSpotListDto;
import capstone.jejuTourrecommend.wishList.domain.service.response.FavoriteSpotsDto;
import capstone.jejuTourrecommend.wishList.domain.service.response.TopTenRecommendedSpotsDto;
import capstone.jejuTourrecommend.wishList.domain.service.response.WishListDto;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.RecommendRouteSpotsRequest;
import capstone.jejuTourrecommend.wishList.presentation.dto.response.FavoriteSpotsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteQueryUseCase {

	WishListDto getFavoriteList(Long memberId, Pageable pageable);

	TopTenRecommendedSpotsDto recommendSpotList(Long favoriteId, List<Long> spotIdList);

	FavoriteSpotsDto favoriteSpotList(Long favoriteId);

	void deleteFavoriteList(Long favoriteId);
}
