package capstone.jejuTourrecommend.wishList.infrastructure.repository;

import java.util.List;

public interface FavoriteSpotQuerydslRepository {

	List<Long> getSpotIdByFavoriteSpot(Long memberId, List<Long> spotIdList);

	List<Long> getSpotIdList(Long favoriteId,List<Long> spotIdList);

}
