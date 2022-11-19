package capstone.jejuTourrecommend.wishList.domain.service;

import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteDto;
import capstone.jejuTourrecommend.wishList.domain.service.request.FavoriteSpotSaveDto;
import capstone.jejuTourrecommend.wishList.domain.service.request.WishListSaveDto;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.FavoriteSpotSaveRequest;

public interface FavoriteCommandUseCase {

	void postFavoriteForm(FavoriteSpotSaveDto favoriteSpotSaveDto);

	FavoriteDto newFavoriteList(WishListSaveDto wishListSaveDto);

	void deleteSpotInFavoriteList(Long favoriteId, Long spotId);

}
