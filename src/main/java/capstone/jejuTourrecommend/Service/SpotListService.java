package capstone.jejuTourrecommend.Service;

import capstone.jejuTourrecommend.Service.spotList.*;
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

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SpotListService {


    private final SpotRepository spotRepository;

    public ResultSpotListDto searchSpotListContains(Long memberId, String spotName, Pageable pageable) {


        Page<SpotListDto> spotListDtos = spotRepository.searchBySpotNameContains(memberId, spotName, pageable);

        return new ResultSpotListDto(200l, true, "성공", spotListDtos);

    }


    public ResultSpotListDto postSpotList(MainPageForm mainPageForm, Long memberId, Pageable pageable) {
        
        List locationList = findLocationList(mainPageForm);

        Category category = findCategory(mainPageForm);

        log.info("location = {}", locationList);
        log.info("category = {} ", category);
        log.info("UserWeightDto() = {}", mainPageForm.getUserWeight());//Todo: 업데이트



        Page<SpotListDto> result;

        //가중치 존재 유무
        result = isPriorityExist(mainPageForm, pageable, locationList, category, memberId);

        return new ResultSpotListDto(200l, true, "성공", result);
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

    public List findLocationList(MainPageForm mainPageForm) {
        log.info("mainPageLocation = {}", mainPageForm.getLocation());

        if (!StringUtils.hasText(mainPageForm.getLocation())) {

            throw new UserException("지역에 null 값이 들어갔습니다");
        }

        /**
         * 북 : 애월읍,제주시,조천읍,구좌읍,우도면
         * 동 : 남원읍, 표선면, 성산읍
         * 서 : 한림읍, 한경면, 대정읍, 안덕면
         * 남 : 서귀포시
         */
        LocationStrategy locationStrategy;

        String location = mainPageForm.getLocation();

        switch (location) {
            case "북부":
                locationStrategy = new NorthLocation();
                break;
            case "동부":
                locationStrategy = new EastLocation();
                break;
            case "서부":
                locationStrategy = new WestLocation();
                break;
            case "남부":
                locationStrategy = new SouthLocation();
                break;
            case "전체":
                locationStrategy = new DefaultLocation();
                break;
            default:
                throw new UserException("카테고리의 제대로된 입력값을 넣어야 합니다");
        }

        return locationStrategy.getLocation();
    }

    private Page<SpotListDto> isPriorityExist(MainPageForm mainPageForm, Pageable pageable, List locationList, Category category, Long memberId) {

        Page<SpotListDto> result;
        if (mainPageForm.getUserWeight() == null ||
                mainPageForm.getUserWeight().get("viewWeight").doubleValue() == 0 &&
                        mainPageForm.getUserWeight().get("priceWeight").doubleValue() == 0 &&
                        mainPageForm.getUserWeight().get("facilityWeight").doubleValue() == 0 &&
                        mainPageForm.getUserWeight().get("surroundWeight").doubleValue() == 0
        ) {
            result = spotRepository.searchSpotByLocationAndCategory(memberId,
                    locationList, category, pageable);

        } else {//사용자가 가중치를 넣은 경우
            UserWeightDto userWeightDto = new UserWeightDto(//Todo: 업데이트
                    mainPageForm.getUserWeight().get("viewWeight").doubleValue(),
                    mainPageForm.getUserWeight().get("priceWeight").doubleValue(),
                    mainPageForm.getUserWeight().get("facilityWeight").doubleValue(),
                    mainPageForm.getUserWeight().get("surroundWeight").doubleValue()
            );

            result = spotRepository.searchSpotByUserPriority(
                    memberId, locationList, userWeightDto, pageable);

        }
        return result;
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


}




