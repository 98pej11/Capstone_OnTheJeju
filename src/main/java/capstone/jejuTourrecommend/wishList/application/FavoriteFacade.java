package capstone.jejuTourrecommend.wishList.application;

import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteDto;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import capstone.jejuTourrecommend.wishList.domain.dto.RouteSpotListDto;
import capstone.jejuTourrecommend.wishList.domain.service.FavoriteCommandUseCase;
import capstone.jejuTourrecommend.wishList.domain.service.FavoriteQueryUseCase;
import capstone.jejuTourrecommend.wishList.domain.service.request.FavoriteSpotSaveDto;
import capstone.jejuTourrecommend.wishList.domain.service.request.WishListSaveDto;
import capstone.jejuTourrecommend.wishList.domain.service.response.FavoriteSpotsDto;
import capstone.jejuTourrecommend.wishList.domain.service.response.TopTenRecommendedSpotsDto;
import capstone.jejuTourrecommend.wishList.domain.service.response.WishListDto;
import capstone.jejuTourrecommend.wishList.presentation.dto.response.FavoriteSpotsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteFacade {

	private final FavoriteCommandUseCase favoriteCommandUseCase;

	private final FavoriteQueryUseCase favoriteQueryUseCase;

	public void postFavoriteForm(FavoriteSpotSaveDto favoriteSpotSaveDto) {
		favoriteCommandUseCase.postFavoriteForm(favoriteSpotSaveDto);
	}

	public FavoriteDto newFavoriteList(WishListSaveDto wishListSaveDto) {
		return favoriteCommandUseCase.newFavoriteList(wishListSaveDto);
	}

	public void deleteSpotInFavoriteList(Long favoriteId, Long spotId) {
		favoriteCommandUseCase.deleteSpotInFavoriteList(favoriteId, spotId);
	}

	public WishListDto getFavoriteList(Long memberId, Pageable pageable) {
		return favoriteQueryUseCase.getFavoriteList(memberId, pageable);
	}

	public void deleteFavoriteList(Long favoriteId) {

		favoriteQueryUseCase.deleteFavoriteList(favoriteId);
	}

	public TopTenRecommendedSpotsDto recommendSpotList(Long favoriteId, List<Long> spotIdList) {

		return favoriteQueryUseCase.recommendSpotList(favoriteId, spotIdList);

	}

	public FavoriteSpotsDto favoriteSpotList(Long favoriteId) {
		return favoriteQueryUseCase.favoriteSpotList(favoriteId);

	}

}
