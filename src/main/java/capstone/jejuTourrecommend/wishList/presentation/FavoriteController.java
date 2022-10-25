package capstone.jejuTourrecommend.wishList.presentation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import capstone.jejuTourrecommend.authentication.LoginUser;
import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.common.GlobalDto;
import capstone.jejuTourrecommend.wishList.application.FavoriteFacade;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteDto;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.FavoriteForm;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.FavoriteNewForm;
import capstone.jejuTourrecommend.wishList.presentation.dto.response.FavoriteListFinalDto;
import capstone.jejuTourrecommend.wishList.presentation.dto.response.NewFavoriteListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FavoriteController {

	private final FavoriteFacade favoriteFacade;

	/**
	 * 선택한 관광지를 선태한 위시리스트에 추가
	 * 선택한 관광지 정보, 사용자 정보, 위시리스트 정보 필요
	 *
	 * @param favoriteForm
	 * @return
	 */
	@PostMapping("/user/favorite/form")
	public GlobalDto postFavoriteForm(//@RequestHeader("ACCESS-TOKEN") String accesstoken,
		@RequestBody FavoriteForm favoriteForm) {

		favoriteFacade.postFavoriteForm(favoriteForm);

		return new GlobalDto(200l, true, "성공");

	}

	/**
	 * 새로운 위시 리스트를 만들고 해당 관광지 넣기
	 * 선택한 관광지 정보, 사용자 정보, 위시리스트 이름 필요
	 *
	 * @param form
	 * @return
	 */
	@PostMapping("/user/favorite/new")
	public NewFavoriteListDto newFavoriteList(
		@RequestBody FavoriteNewForm form,
		@LoginUser Member member) {

		Long spotId = form.getSpotId();
		String favoriteName = form.getFavoriteName();

		FavoriteDto favoriteDto = favoriteFacade.newFavoriteList(member, spotId, favoriteName);

		return new NewFavoriteListDto(200l, true, "성공", favoriteDto);
	}

	/**
	 * 위시 리스트 페이지
	 * 사용자 정보 필요
	 *
	 * @param pageable
	 * @return
	 */
	@GetMapping("/user/favoriteList")
	public FavoriteListFinalDto favoriteList(Pageable pageable, @LoginUser Member member) {

		Page<FavoriteListDto> favoriteList = favoriteFacade.getFavoriteList(member.getId(), pageable);

		return new FavoriteListFinalDto(200l, true, "성공,", favoriteList);

	}

	/**
	 * 위시 리스트 삭제하기
	 * 해당 위시리스트 정보 필요
	 *
	 * @param favoriteId
	 * @return
	 */
	@DeleteMapping("/user/favoriteList/{favoriteId}")
	public GlobalDto deleteFavoriteList(@PathVariable("favoriteId") Long favoriteId) {

		favoriteFacade.deleteFavoriteList(favoriteId);

		return new GlobalDto(200l, true, "성공");

	}

	@DeleteMapping("/user/favoriteList/deleteSpot")
	public GlobalDto deleteSpotInFavoriteList(@RequestParam Long favoriteId,
		@RequestParam Long spotId) {

		favoriteFacade.deleteSpotInFavoriteList(favoriteId, spotId);

		return new GlobalDto(200l, true, "성공");
	}

}












