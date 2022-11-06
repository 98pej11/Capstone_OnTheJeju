package capstone.jejuTourrecommend.wishList.domain.repository;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.wishList.domain.Favorite;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository {

	Page<Favorite> findByMember(Member member, Pageable pageable);

	Optional<Favorite> findOptionById(Long favoriteId);

	Optional<Favorite> findOptionByNameAndMemberId(String favoriteName, Long memberId);

	Optional<Favorite> findByNameAndMember(String favoriteName, Member member);

	void deleteById(Long favoriteId);

	void save(Favorite favorite);

	List<Long> findFavoriteIdListByMemberId(@Param("memberId") Long memberId);

	Boolean isFavoriteSpot(Long memberId, Long spotId, List<Long> favoriteList);

	Page<FavoriteListDto> getFavoriteList(Long memberId, Pageable pageable);

}
