package capstone.jejuTourrecommend.wishList.infrastructure.repository;

import capstone.jejuTourrecommend.wishList.domain.dto.ScoreSumDto;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.RouteForm;

import java.util.List;

public interface FavoriteSpotQuerydslRepository {

	List<Long> getBooleanFavoriteSpot(Long memberId, List<Long> spotIdList);

	List<Long> getSpotIdList(Long favoriteId, RouteForm routeForm);

	ScoreSumDto getScoreSumDto(List<Long> spotIdList);

}
