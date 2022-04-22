package capstone.jejuTourrecommend.web.mainPage;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SpotListController {

    //@PostMapping("/user/spotList")
    public String order(@ModelAttribute SpotSearch spotSearch){

        return "ae";
    }


}
