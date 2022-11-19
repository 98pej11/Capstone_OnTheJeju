package capstone.jejuTourrecommend.wishList.presentation;

import capstone.jejuTourrecommend.wishList.application.FavoriteFacade;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.RecommendRouteSpotsRequest;
import capstone.jejuTourrecommend.wishList.presentation.dto.response.FavoriteSpotsResponse;
import capstone.jejuTourrecommend.wishList.presentation.dto.response.TopTenRecommendedSpotsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RouteController {

	private final FavoriteFacade favoriteFacade;

	@GetMapping("/user/route/spot/{favoriteId}")
	public FavoriteSpotsResponse favoriteRoute(@PathVariable("favoriteId") Long favoriteId) {
		return FavoriteSpotsResponse.from(200l, true, "성공", favoriteFacade.favoriteSpotList(favoriteId));
	}

	@PostMapping("/user/route/topList/{favoriteId}")
	public TopTenRecommendedSpotsResponse topList(@PathVariable("favoriteId") Long favoriteId, @RequestBody RecommendRouteSpotsRequest recommendRouteSpotsRequest) {
		return TopTenRecommendedSpotsResponse.from(200l, true, "성공",
			favoriteFacade.recommendSpotList(favoriteId, recommendRouteSpotsRequest.getSpotIdList()));

	}

}



















