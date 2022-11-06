package capstone.jejuTourrecommend.domain.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.authentication.infrastructure.respository.MemberJpaRepository;
import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.spot.domain.Score;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Picture;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Review;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.SpotDetailDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.service.DetailSpotQueryService;
import capstone.jejuTourrecommend.spot.domain.mainSpot.Location;
import capstone.jejuTourrecommend.spot.domain.mainSpot.MemberSpot;
import capstone.jejuTourrecommend.wishList.domain.Favorite;
import capstone.jejuTourrecommend.wishList.domain.FavoriteSpot;
import capstone.jejuTourrecommend.wishList.infrastructure.repository.FavoriteJpaRepository;
import capstone.jejuTourrecommend.wishList.infrastructure.repository.FavoriteSpotJpaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@Transactional
class DetailSpotQueryServiceTest {

	@Autowired
	DetailSpotQueryService detailSpotQueryService;

	@Autowired
	FavoriteSpotJpaRepository favoriteSpotJpaRepository;

	@Autowired
	FavoriteJpaRepository favoriteJpaRepository;

	@Autowired
	MemberJpaRepository memberJpaRepository;

	@PersistenceContext
	EntityManager em;

	@BeforeEach
	public void init() {

		//String encodedPassword = passwordEncoder.encode("1234");
		//log.info("password = {}",encodedPassword);

		Member member1 = new Member("member1", "member1@gmail.com", "1234");
		Member member2 = new Member("member2", "member2@naver.com", "2345");
		em.persist(member1);
		em.persist(member2);
		log.info("member1 ={}", member1);

		MemberSpot[] memberSpots = new MemberSpot[100];
		Score[] scores = new Score[100];
		Spot[] spots = new Spot[100];
		Picture[][] pictures = new Picture[100][3];
		Review[] reviews = new Review[100];

		Favorite favorite = new Favorite("1일차", member1);
		em.persist(favorite);

		FavoriteSpot[] favoriteSpots = new FavoriteSpot[100];

		for (int i = 0; i < 100; i++) { //지역하고 score만

			if (0 <= i && i < 25) {
				spots[i] = new Spot(Location.Aewol_eup, createScore(scores, i));
				memberSpots[i] = new MemberSpot(0d, member2, spots[i]);
			}
			if (25 <= i && i < 50) {
				spots[i] = new Spot(Location.Aewol_eup, createScore(scores, i));
				memberSpots[i] = new MemberSpot(0d, member2, spots[i]);
			}
			if (50 <= i && i < 75) {
				spots[i] = new Spot(Location.Andeok_myeon, createScore(scores, i));
				memberSpots[i] = new MemberSpot(0d, member1, spots[i]);
			}
			if (75 <= i && i < 100) {
				spots[i] = new Spot(Location.Andeok_myeon, createScore(scores, i));
				memberSpots[i] = new MemberSpot(0d, member1, spots[i]);
			}
			for (int j = 0; j < 3; j++) {
				pictures[i][j] = new Picture("asdf1", spots[i]);
				em.persist(pictures[i][j]);
			}
			em.persist(spots[i]);
			em.persist(memberSpots[i]);

			reviews[i] = new Review("content", spots[0]);
			em.persist(reviews[i]);

			favoriteSpots[i] = new FavoriteSpot(favorite, spots[i]);
			em.persist(favoriteSpots[i]);

		}

		em.flush();
		em.clear();

		//member1 = Member(id=1, username=member1, email=member1@gmail.com, password=1234)
		log.info("member1 = {}", member1);

		log.info("favorite = {}", favorite);   //favorite = Favorite(id=3, name=1일차)

		//spots[0] = Spot(id=8, address=null, description=null, location=Aewol_eup..
		log.info("spots[0] = {}", spots[0]);

		log.info("favoriteSpots[0] = {}", favoriteSpots[0]);//favoriteSpots[0] = FavoriteSpot(id=11, count=0)

	}

	public Score createScore(Score[] scores, int i) {
		Random random = new Random();
		scores[i] = new Score(
			random.nextDouble() * 10, random.nextDouble() * 10,
			random.nextDouble() * 10, random.nextDouble() * 10,
			random.nextDouble() * 10, random.nextDouble() * 10,
			random.nextDouble() * 10, random.nextDouble() * 10,
			random.nextDouble() * 10);
		em.persist(scores[i]);//여기서 이거 한해도 되는 이유는
		// cascade = CascadeType.ALL를 해놓아서 그런거임, 원래는 넣어줘야 함
		return scores[i];
	}

	//Long spotId, String memberEmail, Pageable pageable
	@Test
	public void spotPageTest() throws Exception {
		//given

		String memberEmail = "member1@gmail.com";

		Member member = memberJpaRepository.findOptionByEmail(memberEmail)
			.orElseThrow(() -> new UserException("가입되지 않은 E-MAIL 입니다."));
		Optional<Favorite> favorite = favoriteJpaRepository.findOptionByNameAndMemberId("1일차", member.getId());

		List<FavoriteSpot> byFavoriteId = favoriteSpotJpaRepository.findByFavoriteId(favorite.get().getId());

		//when
		SpotDetailDto spotDetailDto = detailSpotQueryService.spotPage(byFavoriteId.get(0).getSpot().getId(),
			member.getId());

		log.info("spotDto = {}", spotDetailDto);

		//then
	}

}
