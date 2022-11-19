package capstone.jejuTourrecommend.spot.presentation;

import capstone.jejuTourrecommend.authentication.LoginUser;
import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.common.metaDataBuilder.DefaultMetaDataBuilder;
import capstone.jejuTourrecommend.common.metaDataBuilder.MetaData;
import capstone.jejuTourrecommend.common.metaDataBuilder.MetaDataDirector;
import capstone.jejuTourrecommend.spot.application.SpotListFacade;
import capstone.jejuTourrecommend.spot.presentation.request.MainPageRequest;
import capstone.jejuTourrecommend.spot.presentation.request.SpotSearchRequest;
import capstone.jejuTourrecommend.spot.presentation.response.SpotListResponse;
import capstone.jejuTourrecommend.spot.presentation.response.SpotsMetaDataOp;
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
	public SpotListResponse getUserSpotList(@RequestBody MainPageRequest mainPageRequest,
											Pageable pageable,
											@LoginUser Member member) {

		SpotListResponse spotListResponse = spotListFacade
			.getUserSpotList(mainPageRequest, member.getId(), pageable);

		return spotListResponse;

	}

	@PostMapping("/user/spotList/search")
	public SpotListResponse searchSpotListContains(@RequestBody SpotSearchRequest spotSearchRequest,
												   Pageable pageable, @LoginUser Member member) {

		if (!StringUtils.hasText(spotSearchRequest.getSpotName())) {
			throw new UserException("관광지 이름에 빈 문자열이 왔습니다");
		}

		SpotListResponse spotListResponse = spotListFacade.searchSpotListContains(member.getId(),
			spotSearchRequest.getSpotName(), pageable);

		return spotListResponse;
	}

	@GetMapping("/spotList/metaDataOp")
	public SpotsMetaDataOp getMetaData() {

		MetaDataDirector metaDataDirector = new MetaDataDirector(new DefaultMetaDataBuilder());
		metaDataDirector.categoryMetaData();
		MetaData metaData2 = metaDataDirector.locationMetaData();

		return new SpotsMetaDataOp(200l, true, metaData2.getMetaDataList());

	}


}




