package capstone.jejuTourrecommendV1.repository;

import capstone.jejuTourrecommendV1.domain.Favorite;
import capstone.jejuTourrecommendV1.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {

    Page<Favorite> findByMember(Member member, Pageable pageable);

    Optional<Favorite> findOptionById(Long favoriteId);

    Optional<Favorite> findOptionByName(String favoriteName);

    //@Query("select f from Favorite f where f.name= :favoriteName and f.member= : member")
    //Optional<Favorite> findOptionByFavoriteNameAndMember(@Param("favoriteName") String favoriteName, @Param("member") Member member);



}











