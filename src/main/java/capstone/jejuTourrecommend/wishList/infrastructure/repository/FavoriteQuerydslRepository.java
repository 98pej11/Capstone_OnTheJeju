package capstone.jejuTourrecommend.wishList.infrastructure.repository;

import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteQuerydslRepository {

	Boolean isFavoriteSpot(Long memberId, Long spotId);

	Page<FavoriteListDto> getFavoriteList(Long memberId, Pageable pageable);

}
