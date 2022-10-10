package capstone.jejuTourrecommend.spot.presentation;

import capstone.jejuTourrecommend.common.metaDataBuilder.DefaultMetaDataBuilder;
import capstone.jejuTourrecommend.common.metaDataBuilder.MetaDataDirector;
import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ReviewDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.SpotDetailDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.SpotMetaDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.service.SpotService;
import capstone.jejuTourrecommend.spot.presentation.response.ReviewListDto;
import capstone.jejuTourrecommend.spot.presentation.response.SpotListMetaDataOp;
import capstone.jejuTourrecommend.spot.presentation.response.SpotPageDto;
import capstone.jejuTourrecommend.authentication.LoginUser;
import capstone.jejuTourrecommend.config.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RestController
public class SpotController {

    private final SpotService spotService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/user/spot/{spotId}")
    public SpotPageDto spotDetail(@PathVariable("spotId") Long spotId,
                                  @LoginUser Member member){

        log.info("spotId = {}",spotId);
        //여기서 spotId를 도메인 클래스 컨버터 사용가능 (jpa 실절)

        //이거 실험용 데이터임 TODO: 실험용 데이터임
        //Long spotIdTest = 8l;
        //String memberEmailTest = "member1@gmail.com";

        //여기서 토큰으로 역할(role) 조회 가능함(header에서 토큰 가져와야함) TODO: 실제 운영할 코드임


        SpotDetailDto spotDetailDto = spotService.spotPage(spotId, member.getId());

        return new SpotPageDto(200l,true,"성공",spotDetailDto);

    }

    @GetMapping("/user/spot/review/{spotId}")
    public ReviewListDto reviewPage(@PathVariable("spotId") Long spotId,
                                    Pageable pageable){
        log.info("spotId = {}",spotId);

        Page<ReviewDto> reviewDtos = spotService.reviewPage(spotId, pageable);

        return new ReviewListDto(200l,true,"성공",reviewDtos);


    }


    @GetMapping("/spot/metaData")
    public SpotMetaDto getMetaData(){

        return getCategoryMetaData();

    }

    @GetMapping("/spot/metaDataOp")
    public SpotListMetaDataOp getMetaDataOp(){

        MetaDataDirector metaDataDirector = new MetaDataDirector(new DefaultMetaDataBuilder());

        return new SpotListMetaDataOp(200l, true, metaDataDirector.categoryMetaData().getMetaDataList());
    }
//  


    private SpotMetaDto getCategoryMetaData() {
        Map map;
        List list = new ArrayList();

        map = new LinkedHashMap();
        map.put("id",1);
        map.put("name","뷰");
        list.add(map);

        map = new LinkedHashMap();
        map.put("id",2);
        map.put("name","가격");
        list.add(map);

        map = new LinkedHashMap();
        map.put("id",3);
        map.put("name","편의시설");
        list.add(map);

        map = new LinkedHashMap();
        map.put("id",4);
        map.put("name","서비스");
        list.add(map);

        return new  SpotMetaDto(200l, true, list);
    }


}