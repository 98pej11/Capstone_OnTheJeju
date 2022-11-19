package capstone.jejuTourrecommend.wishList.domain.service;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.authentication.infrastructure.respository.MemberJpaRepository;
import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Picture;
import capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot.SpotJpaRepository;
import capstone.jejuTourrecommend.wishList.domain.Favorite;
import capstone.jejuTourrecommend.wishList.domain.FavoriteSpot;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteDto;
import capstone.jejuTourrecommend.wishList.domain.repository.FavoriteRepository;
import capstone.jejuTourrecommend.wishList.domain.repository.FavoriteSpotRepository;
import capstone.jejuTourrecommend.wishList.domain.service.request.FavoriteSpotSaveDto;
import capstone.jejuTourrecommend.wishList.domain.service.request.WishListSaveDto;
import capstone.jejuTourrecommend.wishList.infrastructure.repository.FavoriteJpaRepository;
import capstone.jejuTourrecommend.wishList.infrastructure.repository.FavoriteSpotJpaRepository;
import capstone.jejuTourrecommend.wishList.infrastructure.repository.FavoriteSpotQuerydslRepositoryImpl;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.FavoriteSpotSaveRequest;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.WishListSaveRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class FavoriteCommandServiceTest {

	@Autowired
	FavoriteCommandService favoriteCommandService;

	@Autowired
	MemberJpaRepository memberJpaRepository;
	@Autowired
	FavoriteJpaRepository favoriteJpaRepository;
	@Autowired
	SpotJpaRepository spotJpaRepository;
	@Autowired
	FavoriteSpotJpaRepository favoriteSpotJpaRepository;
	@Autowired
	FavoriteSpotQuerydslRepositoryImpl favoriteSpotQuerydslRepositoryImpl;

	@Autowired
	FavoriteRepository favoriteRepository;
	@Autowired
	FavoriteSpotRepository favoriteSpotRepository;


	@PersistenceContext
	EntityManager em;

	@BeforeEach
	public void testData() {

		Member member = new Member("leoJoo", "em@naver.com", "1234");
		em.persist(member);

		Favorite favorite1 = new Favorite("1일차", member);
		Favorite favorite2 = new Favorite("2일차", member);
		Favorite favorite3 = new Favorite("3일차", member);
		Favorite favorite4 = new Favorite("4일차", member);
		Favorite favorite5 = new Favorite("5일차", member);
		em.persist(favorite1);
		em.persist(favorite2);
		em.persist(favorite3);
		em.persist(favorite4);
		em.persist(favorite5);
		em.persist(favorite5);
		log.info("favorite1 = {}", favorite1);//1
		log.info("favorite5 = {}", favorite5);//5

		Spot[] spots = new Spot[15];
		for (int i = 0; i < 15; i++) {
			spots[i] = new Spot("관광지 " + i);

			em.persist(spots[i]);
		}
		log.info("spot0 = {}", spots[0]);  //7번임
		log.info("spot14 = {}", spots[14]);  //15번임

		FavoriteSpot[] favoriteSpots = new FavoriteSpot[15];

		Picture[] pictures = new Picture[15];

		//spot 마다 사진이 하니씩 있음
		for (int i = 0; i < 5; i++) {
			favoriteSpots[i] = new FavoriteSpot(favorite1, spots[i]);
			em.persist(favoriteSpots[i]);
			pictures[i] = new Picture("숫자" + i, spots[i]);
			em.persist(pictures[i]);
		}
		for (int i = 5; i < 10; i++) {
			favoriteSpots[i] = new FavoriteSpot(favorite2, spots[i]);
			em.persist(favoriteSpots[i]);
			pictures[i] = new Picture("숫자" + i, spots[i]);
			em.persist(pictures[i]);
		}
		for (int i = 10; i < 15; i++) {
			favoriteSpots[i] = new FavoriteSpot(favorite3, spots[i]);
			em.persist(favoriteSpots[i]);
			pictures[i] = new Picture("숫자" + i, spots[i]);
			em.persist(pictures[i]);
		}

	}

	/**
	 * 선택한 관광지를 선태한 위시리스트에 추가
	 * String memberEmail, Long spotId, Long favoriteId
	 * 사용자의 위시리스트 목록 "폼" 보여주기
	 *
	 * @throws Exception
	 */
	@Test
	public void postFavoriteFormTest() throws Exception {

		Spot spot = spotJpaRepository.findOptionByName("관광지 0")
			.orElseThrow(() -> new UserException("해당 이름의 관광지가 없습니다."));

		Member member = memberJpaRepository.findOptionByEmail("em@naver.com")
			.orElseThrow(() -> new UserException("해당 이름의 회원이 없습니다."));
		Favorite favorite = favoriteJpaRepository.findOptionByNameAndMemberId("5일차", member.getId())
			.orElseThrow(() -> new UserException("해당 이름의 위시리스트가 없습니다."));

		//given
		//String memberEmail = "em@naver.com";
		//7,2 중복된 관관지 존재, 새로운 추가 21,6
		FavoriteSpotSaveRequest favoriteSpotSaveRequest = new FavoriteSpotSaveRequest();

		favoriteSpotSaveRequest.setSpotId(spot.getId());
		favoriteSpotSaveRequest.setFavoriteId(favorite.getId());
		Long spotId = spot.getId();
		Long favoriteId = favorite.getId();

		FavoriteSpotSaveDto favoriteSpotSaveDto = favoriteSpotSaveRequest.toFavoriteSpotSaveDto();

		//when
		favoriteCommandService.postFavoriteForm(favoriteSpotSaveDto);

		Optional<FavoriteSpot> result = favoriteSpotJpaRepository.findOptionBySpotIdAndFavoriteId(spotId, favoriteId);

		assertThat(result).isNotEmpty();

		//

		//then
	}


	/**
	 * 새로운 위시 리스트를 만들고 해당 관광지 넣기
	 * String memberEmail, Long spotId, String favoriteName
	 *
	 * @throws Exception
	 */
	@Test
	public void newFavoriteListTest() throws Exception {
		//given
		String memberEmail = "em@naver.com";

		Spot spot = spotJpaRepository.findOptionByName("관광지 14")
			.orElseThrow(() -> new UserException("해당 이름의 관광지가 없습니다."));
		Long spotId = spot.getId(); //spots[14]);  //15번임

		String favoriteName = "새로운 위시리스트1";

		Member member = memberJpaRepository.findOptionByEmail(memberEmail)
			.orElseThrow(() -> new UserException("가입되지 않은 E-MAIL 입니다."));

		//when

		WishListSaveDto wishListSaveDto = new WishListSaveRequest().toWishListSaveDto(member.getId());
		wishListSaveDto.setFavoriteName(favoriteName);
		wishListSaveDto.setSpotId(spotId);

		FavoriteDto favoriteDto = favoriteCommandService.newFavoriteList(wishListSaveDto);

		Optional<Favorite> favorite = favoriteJpaRepository.findOptionByNameAndMemberId(favoriteName, member.getId());

		Optional<FavoriteSpot> result = favoriteSpotJpaRepository.findOptionBySpotIdAndFavoriteId(spotId,
			favorite.get().getId());

		//then
		assertThat(favorite).isNotEmpty();
		assertThat(result).isNotEmpty();
		log.info("favoriteDto = {}", favoriteDto);
		assertThat(favoriteDto).isNotNull();
	}


}
