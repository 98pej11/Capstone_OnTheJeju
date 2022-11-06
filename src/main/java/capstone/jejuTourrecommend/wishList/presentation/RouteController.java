package capstone.jejuTourrecommend.wishList.presentation;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import capstone.jejuTourrecommend.wishList.application.FavoriteFacade;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.RouteForm;
import capstone.jejuTourrecommend.wishList.presentation.dto.response.ResultFavoriteSpotList;
import capstone.jejuTourrecommend.wishList.presentation.dto.response.ResultTopSpot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RouteController {

	private final FavoriteFacade favoriteFacade;

	@GetMapping("/user/route/spot/{favoriteId}")
	public ResultFavoriteSpotList favoriteRoute(@PathVariable("favoriteId") Long favoriteId) {

		ResultFavoriteSpotList resultFavoriteSpotList = favoriteFacade.favoriteSpotList(favoriteId);

		return resultFavoriteSpotList;
	}

	@PostMapping("/user/route/topList/{favoriteId}")
	public ResultTopSpot topList(@PathVariable("favoriteId") Long favoriteId, @RequestBody RouteForm routeForm) {

		log.info("routeForm.getSpotIdList() = {}", routeForm.getSpotIdList());

		List list = favoriteFacade.recommendSpotList(favoriteId, routeForm);

		return new ResultTopSpot(200l, true, "성공", list);

	}

}



















