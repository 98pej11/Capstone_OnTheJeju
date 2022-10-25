package capstone.jejuTourrecommend.wishList.domain.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.RouteForm;
import capstone.jejuTourrecommend.wishList.presentation.dto.response.ResultFavoriteSpotList;

public interface FavoriteQueryUseCase {

	Page<FavoriteListDto> getFavoriteList(Long memberId, Pageable pageable);

	List recommendSpotList(Long favoriteId, RouteForm routeForm);

	ResultFavoriteSpotList favoriteSpotList(Long favoriteId);

	void deleteFavoriteList(Long favoriteId);
}
