package capstone.jejuTourrecommend.wishList.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.wishList.domain.Favorite;

public interface FavoriteJpaRepository extends JpaRepository<Favorite, Long> {

	Page<Favorite> findByMember(Member member, Pageable pageable);

	Optional<Favorite> findOptionById(Long favoriteId);

	Optional<Favorite> findOptionByNameAndMemberId(String favoriteName, Long memberId);

	//Optional<Favorite> findByName(String favoriteName);

	Optional<Favorite> findByNameAndMember(String favoriteName, Member member);

	//@Query("select f from Favorite f where f.name= :favoriteName and f.member= : member")
	//Optional<Favorite> findOptionByFavoriteNameAndMember(@Param("favoriteName") String favoriteName, @Param("member") Member member);

}











