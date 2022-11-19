package capstone.jejuTourrecommend.wishList.presentation;

import capstone.jejuTourrecommend.authentication.LoginUser;
import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.common.GlobalDto;
import capstone.jejuTourrecommend.wishList.application.FavoriteFacade;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteDto;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import capstone.jejuTourrecommend.wishList.domain.service.response.WishListDto;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.FavoriteSpotSaveRequest;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.WishListSaveRequest;
import capstone.jejuTourrecommend.wishList.presentation.dto.response.WishListResponse;
import capstone.jejuTourrecommend.wishList.presentation.dto.response.WishListSaveResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FavoriteController {

	private final FavoriteFacade favoriteFacade;

	/**
	 * 선택한 관광지를 선태한 위시리스트에 추가
	 * 선택한 관광지 정보, 사용자 정보, 위시리스트 정보 필요
	 *
	 * @param favoriteSpotSaveRequest
	 * @return
	 */
	@PostMapping("/user/favorite/form")
	public GlobalDto postFavoriteForm(@RequestBody FavoriteSpotSaveRequest favoriteSpotSaveRequest) {
		favoriteFacade.postFavoriteForm(favoriteSpotSaveRequest.toFavoriteSpotSaveDto());
		return new GlobalDto(200l, true, "성공");

	}

	/**
	 * 새로운 위시 리스트를 만들고 해당 관광지 넣기
	 * 선택한 관광지 정보, 사용자 정보, 위시리스트 이름 필요
	 *
	 * @return
	 */
	@PostMapping("/user/favorite/new")
	public WishListSaveResultResponse newFavoriteList(@RequestBody WishListSaveRequest wishListSaveRequest, @LoginUser Member member) {
		FavoriteDto favoriteDto = favoriteFacade.newFavoriteList(wishListSaveRequest.toWishListSaveDto(member.getId()));
		return WishListSaveResultResponse.from(200l, true, "성공", favoriteDto);
	}

	/**
	 * 위시 리스트 페이지
	 * 사용자 정보 필요
	 *
	 * @param pageable
	 * @return
	 */
	@GetMapping("/user/favoriteList")
	public WishListResponse favoriteList(Pageable pageable, @LoginUser Member member) {
		WishListDto favoriteList = favoriteFacade.getFavoriteList(member.getId(), pageable);
		return new WishListResponse(200l, true, "성공,", favoriteList.getFavoriteListDtos());
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












