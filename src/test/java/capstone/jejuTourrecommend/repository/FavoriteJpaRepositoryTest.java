package capstone.jejuTourrecommend.repository;

import capstone.jejuTourrecommend.wishList.domain.Favorite;
import capstone.jejuTourrecommend.wishList.domain.FavoriteSpot;
import capstone.jejuTourrecommend.wishList.infrastructure.repository.FavoriteJpaRepository;
import capstone.jejuTourrecommend.wishList.infrastructure.repository.FavoriteSpotJpaRepository;
import capstone.jejuTourrecommend.wishList.infrastructure.repository.FavoriteSpotQuerydslRepositoryImpl;
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

@Slf4j
@SpringBootTest
@Transactional
class FavoriteJpaRepositoryTest {

	@Autowired
	FavoriteJpaRepository favoriteJpaRepository;

	@Autowired
	FavoriteSpotQuerydslRepositoryImpl favoriteSpotQuerydslRepositoryImpl;
	@Autowired
	FavoriteSpotJpaRepository favoriteSpotJpaRepository;

	@PersistenceContext
	EntityManager em;

	@Test
	public void deleteFavorite() throws Exception {
		//given
		Favorite favorite = new Favorite("favoriteName");
		em.persist(favorite);

		em.flush();
		em.clear();

		System.out.println("favorite.getId() = " + favorite.getId());

		Optional<Favorite> favoriteOptional1 = favoriteJpaRepository.findOptionById(favorite.getId());
		assertThat(favoriteOptional1.isEmpty()).isEqualTo(false);

		//when
		favoriteJpaRepository.deleteById(favorite.getId());

		em.flush();
		em.clear();

		Optional<Favorite> favoriteOptional = favoriteJpaRepository.findOptionById(favorite.getId());

		//then
		assertThat(favoriteOptional.isEmpty()).isEqualTo(true);

		//assertThat(favorite.getId()).isEqualTo(1l);

	}

	@Test
	public void deleteTest() {
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
		favoriteSpotJpaRepository.deleteAllByFavoriteId(favorite1.getId());
		//원래는 이 기능을 실행하고 나서는 em.flush, em.clear 해줘야 영속성 컨텍스트 내용과 다른 경우가 안생기는데 여기서
		//그거안써도 되는 이유는 favoriteSpotQueryRepository 에 @Transactional 기능이 있기 때문임
		List<FavoriteSpot> byFavoriteId = favoriteSpotJpaRepository.findByFavoriteId(favorite1.getId());
		System.out.println("byFavoriteId = " + byFavoriteId);

		//Optional<Favorite> optionByName = favoriteRepository.findOptionByName(favorite1.getName());

		//System.out.println("optionByName = " + optionByName.get());

	}

}
















