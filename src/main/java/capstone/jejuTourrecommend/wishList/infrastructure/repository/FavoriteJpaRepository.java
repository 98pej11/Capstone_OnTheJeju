package capstone.jejuTourrecommend.wishList.infrastructure.repository;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.wishList.domain.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteJpaRepository extends JpaRepository<Favorite, Long> {

	Page<Favorite> findByMember(Member member, Pageable pageable);

	Optional<Favorite> findOptionById(Long favoriteId);

	Optional<Favorite> findOptionByNameAndMemberId(String favoriteName, Long memberId);

	//Optional<Favorite> findByName(String favoriteName);

	Optional<Favorite> findByNameAndMember(String favoriteName, Member member);

	@Query("select f.id from Favorite f where f.member.id = :memberId")
	List<Long> findFavoriteIdListByMemberId(@Param("memberId") Long memberId);

}











