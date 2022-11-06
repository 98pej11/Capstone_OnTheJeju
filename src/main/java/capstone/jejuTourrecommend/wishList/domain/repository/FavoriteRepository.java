package capstone.jejuTourrecommend.wishList.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.wishList.domain.Favorite;

public interface FavoriteRepository {

	Page<Favorite> findByMember(Member member, Pageable pageable);

	Optional<Favorite> findOptionById(Long favoriteId);

	Optional<Favorite> findOptionByNameAndMemberId(String favoriteName, Long memberId);

	Optional<Favorite> findByNameAndMember(String favoriteName, Member member);

	void deleteById(Long favoriteId);

	void save(Favorite favorite);

	Boolean isFavoriteSpot(Long memberId, Long spotId);

}
