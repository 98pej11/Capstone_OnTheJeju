package capstone.jejuTourrecommend.wishList.application;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteDto;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import capstone.jejuTourrecommend.wishList.domain.service.FavoriteCommandUseCase;
import capstone.jejuTourrecommend.wishList.domain.service.FavoriteQueryUseCase;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.FavoriteForm;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.RouteForm;
import capstone.jejuTourrecommend.wishList.presentation.dto.response.ResultFavoriteSpotList;
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

	public void postFavoriteForm(FavoriteForm favoriteForm) {
		favoriteCommandUseCase.postFavoriteForm(favoriteForm);
	}

	public FavoriteDto newFavoriteList(Long memberId, Long spotId, String favoriteName) {
		return favoriteCommandUseCase.newFavoriteList(memberId, spotId, favoriteName);
	}

	public void deleteSpotInFavoriteList(Long favoriteId, Long spotId) {
		favoriteCommandUseCase.deleteSpotInFavoriteList(favoriteId, spotId);
	}

	public Page<FavoriteListDto> getFavoriteList(Long memberId, Pageable pageable) {
		return favoriteQueryUseCase.getFavoriteList(memberId, pageable);
	}

	public void deleteFavoriteList(Long favoriteId) {

		favoriteQueryUseCase.deleteFavoriteList(favoriteId);
	}

	public List recommendSpotList(Long favoriteId, RouteForm routeForm) {

		return favoriteQueryUseCase.recommendSpotList(favoriteId, routeForm);

	}

	public ResultFavoriteSpotList favoriteSpotList(Long favoriteId) {
		return favoriteQueryUseCase.favoriteSpotList(favoriteId);

	}

}
