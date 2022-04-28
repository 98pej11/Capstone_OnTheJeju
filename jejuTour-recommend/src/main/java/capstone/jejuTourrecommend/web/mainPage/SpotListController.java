package capstone.jejuTourrecommend.web.mainPage;


import capstone.jejuTourrecommend.domain.Category;
import capstone.jejuTourrecommend.domain.Location;
import capstone.jejuTourrecommend.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SpotListController {

    private final SpotRepository spotRepository;

    @GetMapping("/spotList")
    public ResultSpotListDto getSpot(@RequestBody MainPageForm mainPageForm,Pageable pageable){

        //아래 두줄은 내가 실험 넣은 코드임
        Location location = null;
        Category category = null;
        log.info("location = {} ",location);
        log.info("category = {} ",category);

//        mainPageForm.setPage(0);
//        mainPageForm.setPage(10);
//        PageRequest pageRequest = PageRequest.of(mainPageForm.getPage(), mainPageForm.getSize());


        Page<SpotLocationDto> result = spotRepository.searchSpotByLocationAndCategory(location, category, pageable);
        return new ResultSpotListDto(200l,true,"성공",result);

    }


    //프런트에서 객체단위로 줄수 있나?//그냥 변수 이름만 맞추면 되나?
    @PostMapping("/spotList")//일단 토큰은 배재하고 검색해보자
    public ResultSpotListDto postSpot(@RequestBody MainPageForm mainPageForm,Pageable pageable){

        Long memberId=1l;
        log.info("memberId = {}",memberId);
        log.info("pageable = {}",pageable);

        Location location = findLocation(mainPageForm);
        Category category = findCategory(mainPageForm);

        //mainPageForm.setLocation(Location.Andeok_myeon);
        log.info("location = {} ",location);
        //mainPageForm.setCategory(Category.VIEW);
        log.info("category = {} ",category);
        log.info("mainPageForm.getUserWeightDto() = {}",mainPageForm.getUserWeight());



//        mainPageForm.setPage(0);
//        mainPageForm.setPage(10);
//        PageRequest pageRequest = PageRequest.of(mainPageForm.getPage(), mainPageForm.getSize());

        if(mainPageForm.getUserWeight()==null) {
            Page<SpotLocationDto> result = spotRepository.searchSpotByLocationAndCategory(
                    location, category, pageable);
            return new ResultSpotListDto(200l,true,"성공",result);
        }
        else {
            UserWeightDto userWeightDto = new UserWeightDto(
                    mainPageForm.getUserWeight().get("viewWeight").doubleValue(),
                    mainPageForm.getUserWeight().get("priceWeight").doubleValue(),
                    mainPageForm.getUserWeight().get("facilityWeight").doubleValue(),
                    mainPageForm.getUserWeight().get("surroundWeight").doubleValue()
            );
            log.info("userWeightDto = {}",userWeightDto);

            Page<SpotLocationDto> resultPriority = spotRepository.searchSpotByUserPriority(
                    memberId,location, userWeightDto, pageable);
            return new ResultSpotListDto(200l,true,"성공",resultPriority);

        }
    }

    private Category findCategory(MainPageForm mainPageForm) {
        log.info("mainPageCategory = {} ",mainPageForm.getCategory());
        Category category = null;

        //log.info("isTrue? = {}",StringUtils.hasText(mainPageForm.getCategory()));
        if(!StringUtils.hasText(mainPageForm.getCategory())){
            log.info("카테고리 null에 들어감");
            //category = Category.VIEW;   //기본값은 뷰로 한다
            return category;
        }

        if(mainPageForm.getCategory().equals("view")) {
            category = Category.VIEW;
        }
        else if(mainPageForm.getCategory().equals("price"))
            category = Category.PRICE;
        else if(mainPageForm.getCategory().equals("facility"))
            category = Category.FACILITY;
        else if(mainPageForm.getCategory().equals("surround"))
            category = Category.SURROUND;
        else {
            category = Category.VIEW;   //기본값은 뷰로 한다
        }

        return category;
    }

    private Location findLocation(MainPageForm mainPageForm) {
        log.info("mainPageLocation = {}",mainPageForm.getLocation());
        Location location=null;
        //log.info("location = {}",location);

        if(!StringUtils.hasText(mainPageForm.getLocation())){
            log.info("카테고리 null에 들어감");
            //category = Category.VIEW;   //기본값은 뷰로 한다
            return location;
        }

        if(mainPageForm.getLocation().equals("제주시"))
            location = Location.Jeju_si;
        else if(mainPageForm.getLocation().equals("애월읍"))
             location = Location.Aewol_eup;
        else if(mainPageForm.getLocation().equals("한림읍"))
             location = Location.Hallim_eup;
        else if(mainPageForm.getLocation().equals("한경면"))
             location = Location.Hangyeong_myeon;
        else if(mainPageForm.getLocation().equals("조천읍"))
             location = Location.Jocheon_eup;
        else if(mainPageForm.getLocation().equals("구좌읍"))
             location = Location.Gujwa_eup;
        else if(mainPageForm.getLocation().equals("대정읍"))
             location = Location.Daejeong_eup;
        else if(mainPageForm.getLocation().equals("안덕면")) {
            location = Location.Andeok_myeon;
            //log.info("location = {} ",location);
        }
        else if(mainPageForm.getLocation().equals("서귀포시"))
             location = Location.Seogwipo_si;
        else if(mainPageForm.getLocation().equals("남원읍"))
             location = Location.Namwon_eup;
        else if(mainPageForm.getLocation().equals("표선면"))
             location = Location.Pyoseon_myeon;
        else if(mainPageForm.getLocation().equals("성산읍"))
             location = Location.Seongsan_eup;
        else if(mainPageForm.getLocation().equals("우도면"))
             location = Location.Udo_myeon;
        else if(mainPageForm.getLocation().equals("추자면"))
             location = Location.Chuja_myeon;
        else {
            location=Location.Jeju_si;
        }

        return location;
    }


}







