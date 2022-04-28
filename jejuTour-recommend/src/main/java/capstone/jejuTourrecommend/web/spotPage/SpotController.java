package capstone.jejuTourrecommend.web.spotPage;

import capstone.jejuTourrecommend.domain.Service.SpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SpotController {

    private final SpotService spotService;

    @GetMapping("/user/spot/{spotId}")
    public SpotPageDto spotDetail(@PathVariable Long spotId){

        Long memberId = 1l;
        Long spotIdTest = 7l;

        SpotDetailDto spotDetailDto = spotService.spotPage(spotIdTest, memberId);

        return new SpotPageDto(200l,true,"성공",spotDetailDto);

    }

}
