package capstone.jejuTourrecommend.domain.Service;

import capstone.jejuTourrecommend.domain.Category;
import capstone.jejuTourrecommend.domain.Location;
import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.repository.MemberRepository;
import capstone.jejuTourrecommend.repository.SpotRepository;
import capstone.jejuTourrecommend.web.mainPage.MainPageForm;
import capstone.jejuTourrecommend.web.mainPage.ResultSpotListDto;
import capstone.jejuTourrecommend.web.mainPage.SpotListDto;
import capstone.jejuTourrecommend.web.mainPage.UserWeightDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SpotListService {


    private final SpotRepository spotRepository;
    private final MemberRepository memberRepository;

    private final EntityManager em;

    public ResultSpotListDto getSpotList(MainPageForm mainPageForm, Pageable pageable){

        Location location = findLocation(mainPageForm);
        Category category = findCategory(mainPageForm);

        log.info("location = {} ",location);
        log.info("category = {} ",category);

//        mainPageForm.setPage(0);
//        mainPageForm.setPage(10);
//        PageRequest pageRequest = PageRequest.of(mainPageForm.getPage(), mainPageForm.getSize());


        Page<SpotListDto> result = spotRepository
                .searchSpotByLocationAndCategory(location, category, pageable);


        return new ResultSpotListDto(200l,true,"성공",result);
    }


    public ResultSpotListDto postSpotList(MainPageForm mainPageForm,String memberEmail, Pageable pageable){

        Location location = findLocation(mainPageForm);
        Category category = findCategory(mainPageForm);

        //mainPageForm.setLocation(Location.Andeok_myeon);
        log.info("location = {} ",location);
        //mainPageForm.setCategory(Category.VIEW);
        log.info("category = {} ",category);
        log.info("mainPageForm.getUserWeightDto() = {}",mainPageForm.getUserWeight());


        //이거 실험용 데이터임 TODO: 실험용 데이터임
//        mainPageForm.setPage(0);
//        mainPageForm.setPage(10);
//        PageRequest pageRequest = PageRequest.of(mainPageForm.getPage(), mainPageForm.getSize());

        if(mainPageForm.getUserWeight()==null) {
            Page<SpotListDto> result = spotRepository.searchSpotByLocationAndCategory(
                    location, category, pageable);

            em.flush();
            em.clear();

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

            Optional<Member> optionByEmail = memberRepository.findOptionByEmail(memberEmail);

            Page<SpotListDto> resultPriority = spotRepository.searchSpotByUserPriority(
                    optionByEmail.get().getId(), location, userWeightDto, pageable);

            em.flush();
            em.clear();

            return new ResultSpotListDto(200l,true,"성공",resultPriority);

        }
    }

    public Category findCategory(MainPageForm mainPageForm) {
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

    public Location findLocation(MainPageForm mainPageForm) {
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




