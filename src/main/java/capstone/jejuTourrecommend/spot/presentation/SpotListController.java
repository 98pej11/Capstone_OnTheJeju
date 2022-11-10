package capstone.jejuTourrecommend.spot.presentation;

import capstone.jejuTourrecommend.authentication.LoginUser;
import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.common.metaDataBuilder.DefaultMetaDataBuilder;
import capstone.jejuTourrecommend.common.metaDataBuilder.MetaData;
import capstone.jejuTourrecommend.common.metaDataBuilder.MetaDataDirector;
import capstone.jejuTourrecommend.spot.application.SpotListFacade;
import capstone.jejuTourrecommend.spot.domain.mainSpot.service.SpotListService;
import capstone.jejuTourrecommend.spot.presentation.request.MainPageForm;
import capstone.jejuTourrecommend.spot.presentation.request.SearchForm;
import capstone.jejuTourrecommend.spot.presentation.response.ResultSpotListDto;
import capstone.jejuTourrecommend.spot.presentation.response.SpotListMetaDataOp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SpotListController {

	private final SpotListFacade spotListFacade;

	@PostMapping("/user/spotList/priority")//일단 토큰은 배재하고 검색해보자
	public ResultSpotListDto getUserSpotList(@RequestBody MainPageForm mainPageForm,
		Pageable pageable,
		@LoginUser Member member) {

		ResultSpotListDto resultSpotListDto = spotListFacade
			.getUserSpotList(mainPageForm, member.getId(), pageable);

		return resultSpotListDto;

	}

	@PostMapping("/user/spotList/search")//일단 토큰은 배재하고 검색해보자
	public ResultSpotListDto searchSpotListContains(@RequestBody SearchForm searchForm,
		Pageable pageable, @LoginUser Member member) {

		if (!StringUtils.hasText(searchForm.getSpotName())) {
			throw new UserException("관광지 이름에 빈 문자열이 왔습니다");
		}

		ResultSpotListDto resultSpotListDto = spotListFacade.searchSpotListContains(member.getId(),
			searchForm.getSpotName(), pageable);

		return resultSpotListDto;
	}

	@GetMapping("/spotList/metaDataOp")
	public SpotListMetaDataOp getMetaData() {

		MetaDataDirector metaDataDirector = new MetaDataDirector(new DefaultMetaDataBuilder());
		metaDataDirector.categoryMetaData();
		MetaData metaData2 = metaDataDirector.locationMetaData();

		return new SpotListMetaDataOp(200l, true, metaData2.getMetaDataList());

	}


}




