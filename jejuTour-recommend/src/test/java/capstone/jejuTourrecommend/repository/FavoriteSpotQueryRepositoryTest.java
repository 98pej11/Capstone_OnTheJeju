package capstone.jejuTourrecommend.repository;

import capstone.jejuTourrecommend.domain.Favorite;
import capstone.jejuTourrecommend.domain.FavoriteSpot;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class FavoriteSpotQueryRepositoryTest {

    @Autowired
    FavoriteSpotQueryRepository favoriteSpotQueryRepository;

    @Autowired
    FavoriteSpotRepository favoriteSpotRepository;

    @PersistenceContext
    EntityManager em;


    @Test
    public void deleteFavoriteSpot() throws Exception{
        //given
        Favorite favorite = new Favorite("favorite");
        em.persist(favorite);

        FavoriteSpot favoriteSpot1 = new FavoriteSpot(favorite);
        FavoriteSpot favoriteSpot2 = new FavoriteSpot(favorite);
        FavoriteSpot favoriteSpot3 = new FavoriteSpot(favorite);
        FavoriteSpot favoriteSpot4 = new FavoriteSpot(favorite);
        FavoriteSpot favoriteSpot5 = new FavoriteSpot(favorite);
        FavoriteSpot favoriteSpot6 = new FavoriteSpot(favorite);

        em.persist(favoriteSpot1);
        em.persist(favoriteSpot2);
        em.persist(favoriteSpot3);
        em.persist(favoriteSpot4);
        em.persist(favoriteSpot5);
        em.persist(favoriteSpot6);

        em.flush();
        em.clear();

        List<FavoriteSpot> favoriteSpotList1 =
                favoriteSpotRepository.findByFavoriteId(favorite.getId());

        assertThat(favoriteSpotList1.size()).isEqualTo(6);


        //when
        favoriteSpotQueryRepository.deleteFavoriteSpot(favorite.getId());

        List<FavoriteSpot> favoriteSpotList2 =
                favoriteSpotRepository.findByFavoriteId(favorite.getId());



        //then
        assertThat(favoriteSpotList2.size()).isEqualTo(0);


    }








}










