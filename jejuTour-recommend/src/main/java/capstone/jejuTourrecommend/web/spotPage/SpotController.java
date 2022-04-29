package capstone.jejuTourrecommend.web.spotPage;

import capstone.jejuTourrecommend.domain.Service.SpotService;
import capstone.jejuTourrecommend.web.login.exceptionClass.UserException;
import capstone.jejuTourrecommend.web.login.exhandler.ErrorResult;
import capstone.jejuTourrecommend.web.login.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/user/spot/{spotId}")
    public SpotPageDto spotDetail(@PathVariable Long spotId,
                                  @RequestHeader("X-AUTH-TOKEN") String accesstoken){

        log.info("spotId = {}",spotId);



        Long spotIdTest = 8l;
        String memberEmailTest = "member1@gmail.com";

        //여기서 토큰으로 역할(role) 조회 가능함(header에서 토큰 가져와야함)
        String memberEmail = jwtTokenProvider.getUserPk(accesstoken);

        SpotDetailDto spotDetailDto = spotService.spotPage(spotIdTest, memberEmail);

        return new SpotPageDto(200l,true,"성공",spotDetailDto);

    }

}
