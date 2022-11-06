package capstone.jejuTourrecommend.spot.presentation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import capstone.jejuTourrecommend.authentication.LoginUser;
import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.common.metaDataBuilder.DefaultMetaDataBuilder;
import capstone.jejuTourrecommend.common.metaDataBuilder.MetaDataDirector;
import capstone.jejuTourrecommend.spot.application.DetailSpotFacade;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ReviewDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.SpotDetailDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.SpotMetaDto;
import capstone.jejuTourrecommend.spot.presentation.response.ReviewListDto;
import capstone.jejuTourrecommend.spot.presentation.response.SpotListMetaDataOp;
import capstone.jejuTourrecommend.spot.presentation.response.SpotPageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SpotController {

	private final DetailSpotFacade detailSpotFacade;

	@GetMapping("/user/spot/{spotId}")
	public SpotPageDto spotDetail(@PathVariable("spotId") Long spotId, @LoginUser Member member) {

		SpotDetailDto spotDetailDto = detailSpotFacade.spotPage(spotId, member.getId());

		return new SpotPageDto(200l, true, "성공", spotDetailDto);

	}

	@GetMapping("/user/spot/review/{spotId}")
	public ReviewListDto reviewPage(@PathVariable("spotId") Long spotId, Pageable pageable) {

		Page<ReviewDto> reviewDtos = detailSpotFacade.reviewPage(spotId, pageable);

		return new ReviewListDto(200l, true, "성공", reviewDtos);

	}


	@GetMapping("/spot/metaDataOp")
	public SpotListMetaDataOp getMetaDataOp() {

		MetaDataDirector metaDataDirector = new MetaDataDirector(new DefaultMetaDataBuilder());

		return new SpotListMetaDataOp(200l, true, metaDataDirector.categoryMetaData().getMetaDataList());
	}

}
