package capstone.jejuTourrecommend.domain.Service;

import capstone.jejuTourrecommend.domain.*;
import capstone.jejuTourrecommend.repository.MemberRepository;
import capstone.jejuTourrecommend.repository.SpotRepository;
import capstone.jejuTourrecommend.web.login.exceptionClass.UserException;
import capstone.jejuTourrecommend.web.pageDto.mainPage.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SpotListService {


    private final SpotRepository spotRepository;
    private final MemberRepository memberRepository;

    private final EntityManager em;


    public ResultSpotListDto searchSpotListContains(String memberEmail, String spotName, Pageable pageable) {

        Member member = memberRepository.findOptionByEmail(memberEmail)
                .orElseThrow(() -> new UserException("가입되지 않은 E-MAIL 입니다."));

        Page<SpotListDto> spotListDtos = spotRepository.searchBySpotNameContains(member.getId(), spotName, pageable);

        return new ResultSpotListDto(200l, true, "성공", spotListDtos);

    }


    public ResultSpotListDto postSpotList(MainPageForm mainPageForm, String memberEmail, Pageable pageable) {

        //Location location = findLocation(mainPageForm);

        List locationList = findLocationList(mainPageForm);

        Category category = findCategory(mainPageForm);

        //mainPageForm.setLocation(Location.Andeok_myeon);
        //log.info("location = {} ",location);
        log.info("location = {}", locationList);
        //mainPageForm.setCategory(Category.VIEW);
        log.info("category = {} ", category);
        log.info("UserWeightDto() = {}", mainPageForm.getUserWeight());//Todo: 업데이트


        Member member = memberRepository.findOptionByEmail(memberEmail)
                .orElseThrow(() -> new UserException("가입되지 않은 E-MAIL 입니다."));

        //이거 실험용 데이터임 TODO: 실험용 데이터임
//        mainPageForm.setPage(0);
//        mainPageForm.setPage(10);
//        PageRequest pageRequest = PageRequest.of(mainPageForm.getPage(), mainPageForm.getSize());

        //사용자가 가중치를 입력 안한 경우
        if (mainPageForm.getUserWeight().get("viewWeight").doubleValue() == 0 &&
                mainPageForm.getUserWeight().get("priceWeight").doubleValue() == 0 &&
                mainPageForm.getUserWeight().get("facilityWeight").doubleValue() == 0 &&
                mainPageForm.getUserWeight().get("surroundWeight").doubleValue() == 0
        ) {//Todo: 업데이트

            Page<SpotListDto> result = spotRepository.searchSpotByLocationAndCategory(member.getId(),
                    locationList, category, pageable);


            return new ResultSpotListDto(200l, true, "성공", result);
        } else {//사용자가 가중치를 넣은 경우
            UserWeightDto userWeightDto = new UserWeightDto(//Todo: 업데이트
                    mainPageForm.getUserWeight().get("viewWeight").doubleValue(),
                    mainPageForm.getUserWeight().get("priceWeight").doubleValue(),
                    mainPageForm.getUserWeight().get("facilityWeight").doubleValue(),
                    mainPageForm.getUserWeight().get("surroundWeight").doubleValue()
            );

            log.info("userWeightDto = {}", userWeightDto);


            Page<SpotListDto> resultPriority = spotRepository.searchSpotByUserPriority(
                    member.getId(), locationList, userWeightDto, pageable);


            return new ResultSpotListDto(200l, true, "성공", resultPriority);

        }
    }


    public Category findCategory(MainPageForm mainPageForm) {
        log.info("mainPageCategory = {} ", mainPageForm.getCategory());
        Category category = null;

        //log.info("isTrue? = {}",StringUtils.hasText(mainPageForm.getCategory()));
        if (!StringUtils.hasText(mainPageForm.getCategory())) {
            throw new UserException("올바른 카테고리를 입력하세요. null 값이 들어갔습니다");
        }

        if (mainPageForm.getCategory().equals("뷰")) {
            category = Category.VIEW;
        } else if (mainPageForm.getCategory().equals("가격"))
            category = Category.PRICE;
        else if (mainPageForm.getCategory().equals("편의시설"))
            category = Category.FACILITY;
        else if (mainPageForm.getCategory().equals("서비스"))
            category = Category.SURROUND;
        else if (mainPageForm.getCategory().equals("전체")) {
            category = null;   //기본값은 뷰로 한다
        } else {
            throw new UserException("올바른 카테고리를 입력하세요");
        }

        return category;
    }

    public Location findLocation(MainPageForm mainPageForm) {
        log.info("mainPageLocation = {}", mainPageForm.getLocation());
        Location location = null;
        //log.info("location = {}",location);

        if (!StringUtils.hasText(mainPageForm.getLocation())) {
            log.info("카테고리 null에 들어감");
            //category = Category.VIEW;   //기본값은 뷰로 한다
            return location;
        }

        if (mainPageForm.getLocation().equals("제주시"))
            location = Location.Jeju_si;
        else if (mainPageForm.getLocation().equals("애월읍"))
            location = Location.Aewol_eup;
        else if (mainPageForm.getLocation().equals("한림읍"))
            location = Location.Hallim_eup;
        else if (mainPageForm.getLocation().equals("한경면"))
            location = Location.Hangyeong_myeon;
        else if (mainPageForm.getLocation().equals("조천읍"))
            location = Location.Jocheon_eup;
        else if (mainPageForm.getLocation().equals("구좌읍"))
            location = Location.Gujwa_eup;
        else if (mainPageForm.getLocation().equals("대정읍"))
            location = Location.Daejeong_eup;
        else if (mainPageForm.getLocation().equals("안덕면")) {
            location = Location.Andeok_myeon;
        } else if (mainPageForm.getLocation().equals("서귀포시"))
            location = Location.Seogwipo_si;
        else if (mainPageForm.getLocation().equals("남원읍"))
            location = Location.Namwon_eup;
        else if (mainPageForm.getLocation().equals("표선면"))
            location = Location.Pyoseon_myeon;
        else if (mainPageForm.getLocation().equals("성산읍"))
            location = Location.Seongsan_eup;
        else if (mainPageForm.getLocation().equals("우도면"))
            location = Location.Udo_myeon;
        else if (mainPageForm.getLocation().equals("추자면"))
            location = Location.Chuja_myeon;
        else {
            location = Location.Jeju_si;
        }

        return location;
    }

    public List findLocationList(MainPageForm mainPageForm) {
        log.info("mainPageLocation = {}", mainPageForm.getLocation());
        List list = null;
        if (!StringUtils.hasText(mainPageForm.getLocation())) {
            log.info("지역에 null 값이 들어갔습니다");///
            throw new UserException("지에 null 값이 들어갔습니다");
        }
        /**
         * 북 : 애월읍,제주시,조천읍,구좌읍,우도면
         * 동 : 남원읍, 표선면, 성산읍
         * 서 : 한림읍, 한경면, 대정읍, 안덕면
         * 남 : 서귀포시
         */


        if (mainPageForm.getLocation().equals("북부")) {
            List northList = Arrays.asList(Location.Aewol_eup, Location.Jeju_si, Location.Jocheon_eup, Location.Gujwa_eup, Location.Udo_myeon);
            return northList;
        } else if (mainPageForm.getLocation().equals("동부")) {
            List eastList = Arrays.asList(Location.Namwon_eup, Location.Pyoseon_myeon, Location.Seongsan_eup);
            return eastList;
        } else if (mainPageForm.getLocation().equals("서부")) {
            List westList = Arrays.asList(Location.Hallim_eup, Location.Hangyeong_myeon, Location.Daejeong_eup, Location.Andeok_myeon);
            return westList;
        } else if (mainPageForm.getLocation().equals("남부")) {
            List southList = Arrays.asList(Location.Seogwipo_si);
            return southList;
        } else if (mainPageForm.getLocation().equals("전체")) {
            List allList = Arrays.asList(Location.Jeju_si, Location.Aewol_eup, Location.Hallim_eup,
                    Location.Hangyeong_myeon, Location.Jocheon_eup, Location.Gujwa_eup,
                    Location.Daejeong_eup, Location.Andeok_myeon, Location.Seogwipo_si,
                    Location.Namwon_eup, Location.Pyoseon_myeon, Location.Seongsan_eup, Location.Udo_myeon
            );
            return allList;
        } else {
            throw new UserException("카테고리의 제대로된 입력값을 넣어야 합니다");
        }


    }


}




