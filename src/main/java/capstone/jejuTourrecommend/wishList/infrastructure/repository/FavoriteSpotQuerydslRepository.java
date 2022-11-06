package capstone.jejuTourrecommend.wishList.infrastructure.repository;

import capstone.jejuTourrecommend.wishList.domain.FavoriteSpot;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import capstone.jejuTourrecommend.wishList.domain.dto.RouteSpotListDto;
import capstone.jejuTourrecommend.wishList.domain.dto.SpotListDtoByFavoriteSpot;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.RouteForm;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteSpotQuerydslRepository {

	List<Long> getBooleanFavoriteSpot(Long memberId, List<Long> spotIdList);

	Page<FavoriteListDto> getFavoriteList(Long memberId, Pageable pageable);

	List<SpotListDtoByFavoriteSpot> favoriteSpotList(Long favoriteId);

	FavoriteSpot existSpot(Long favoriteId, RouteForm routeForm);

	List recommendSpotList(Long favoriteId, RouteForm routeForm);

}
