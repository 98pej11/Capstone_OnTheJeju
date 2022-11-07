package capstone.jejuTourrecommend.wishList.domain.service;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteDto;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.FavoriteForm;

public interface FavoriteCommandUseCase {

	void postFavoriteForm(FavoriteForm favoriteForm);

	FavoriteDto newFavoriteList(Long memberId, Long spotId, String favoriteName);

	void deleteSpotInFavoriteList(Long favoriteId, Long spotId);

}
