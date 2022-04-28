package capstone.jejuTourrecommend.repository;

import capstone.jejuTourrecommend.domain.Favorite;
import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.web.favoritePage.FavoriteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {

    Page<Favorite> findByMember(Member member, Pageable pageable);

    Optional<Favorite> findOptionById(Long favoriteId);



}











