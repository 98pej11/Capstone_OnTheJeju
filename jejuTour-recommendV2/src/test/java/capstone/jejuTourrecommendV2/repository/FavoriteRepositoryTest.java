package capstone.jejuTourrecommendV2.repository;

import capstone.jejuTourrecommendV2.domain.Favorite;
import capstone.jejuTourrecommendV2.domain.FavoriteSpot;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@SpringBootTest
@Transactional
class FavoriteRepositoryTest {

    @Autowired
    FavoriteRepository favoriteRepository;

    @Autowired
    FavoriteSpotQueryRepository favoriteSpotQueryRepository;
    @Autowired
    FavoriteSpotRepository favoriteSpotRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void deleteFavorite() throws Exception{
        //given
        Favorite favorite = new Favorite("favoriteName");
        em.persist(favorite);

        em.flush();
        em.clear();

        System.out.println("favorite.getId() = " + favorite.getId());

        Optional<Favorite> favoriteOptional1 = favoriteRepository.findOptionById(favorite.getId());
        assertThat(favoriteOptional1.isEmpty()).isEqualTo(false);

        //when
        favoriteRepository.deleteById(favorite.getId());

        em.flush();
        em.clear();

        Optional<Favorite> favoriteOptional = favoriteRepository.findOptionById(favorite.getId());

        //then
        assertThat(favoriteOptional.isEmpty()).isEqualTo(true);

        //assertThat(favorite.getId()).isEqualTo(1l);

    }

    @Test
    public void deleteTest(){
        Favorite favorite1 = new Favorite("favoriteName1");
        Favorite favorite2 = new Favorite("favoriteName2");
        Favorite favorite3 = new Favorite("favoriteName3");
        Favorite favorite4 = new Favorite("favoriteName4");
        Favorite favorite5 = new Favorite("favoriteName5");

        em.persist(favorite1);
        em.persist(favorite2);
        em.persist(favorite3);
        em.persist(favorite4);
        em.persist(favorite5);

        FavoriteSpot favoriteSpot1 = new FavoriteSpot(favorite1);
        FavoriteSpot favoriteSpot2 = new FavoriteSpot(favorite1);
        FavoriteSpot favoriteSpot3 = new FavoriteSpot(favorite1);
        FavoriteSpot favoriteSpot4 = new FavoriteSpot(favorite1);
        FavoriteSpot favoriteSpot5 = new FavoriteSpot(favorite1);

        //인단 단건 삭제할때는 영속성 컨텍스트에 있는 것도 삭제됨

        //favoriteRepository.deleteById(favorite1.getId());
        favoriteSpotQueryRepository.deleteFavoriteSpotByFavoriteId(favorite1.getId());
        //원래는 이 기능을 실행하고 나서는 em.flush, em.clear 해줘야 영속성 컨텍스트 내용과 다른 경우가 안생기는데 여기서
        //그거안써도 되는 이유는 favoriteSpotQueryRepository 에 @Transactional 기능이 있기 때문임
        List<FavoriteSpot> byFavoriteId = favoriteSpotRepository.findByFavoriteId(favorite1.getId());
        System.out.println("byFavoriteId = " + byFavoriteId);


        //Optional<Favorite> optionByName = favoriteRepository.findOptionByName(favorite1.getName());

        //System.out.println("optionByName = " + optionByName.get());


    }

}
















