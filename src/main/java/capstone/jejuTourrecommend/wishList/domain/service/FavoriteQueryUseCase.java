package capstone.jejuTourrecommend.wishList.domain.service;

import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.RouteForm;
import capstone.jejuTourrecommend.wishList.presentation.dto.response.ResultFavoriteSpotList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteQueryUseCase {

    Page<FavoriteListDto> getFavoriteList(Long memberId, Pageable pageable);

    List recommendSpotList(Long favoriteId, RouteForm routeForm);

    ResultFavoriteSpotList favoriteSpotList(Long favoriteId);

    void deleteFavoriteList(Long favoriteId);
}
