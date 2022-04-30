package capstone.jejuTourrecommend.web.controller;

import capstone.jejuTourrecommend.domain.Service.SpotService;
import capstone.jejuTourrecommend.web.login.exceptionClass.UserException;
import capstone.jejuTourrecommend.web.login.exhandler.ErrorResult;
import capstone.jejuTourrecommend.web.login.jwt.JwtTokenProvider;
import capstone.jejuTourrecommend.web.spotPage.SpotDetailDto;
import capstone.jejuTourrecommend.web.spotPage.SpotPageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SpotController {

    private final SpotService spotService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/spot/{spotId}")
    public SpotPageDto spotDetail(@PathVariable Long spotId,
                                  @RequestHeader("X-AUTH-TOKEN") String accesstoken,
                                  Pageable pageable){

        log.info("spotId = {}",spotId);
        //여기서 spotId를 도메인 클래스 컨버터 사용가능 (jpa 실절)


        Long spotIdTest = 8l;
        String memberEmailTest = "member1@gmail.com";

        //여기서 토큰으로 역할(role) 조회 가능함(header에서 토큰 가져와야함)
        String memberEmail = jwtTokenProvider.getUserPk(accesstoken);

        SpotDetailDto spotDetailDto = spotService.spotPage(spotIdTest, memberEmailTest,pageable);

        return new SpotPageDto(200l,true,"성공",spotDetailDto);

    }

}
