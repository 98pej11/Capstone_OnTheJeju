package capstone.jejuTourrecommend.spot.application;

import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.spot.application.locationStragety.*;
import capstone.jejuTourrecommend.spot.domain.mainSpot.Category;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.UserWeightDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.service.SpotListCommandUseCase;
import capstone.jejuTourrecommend.spot.domain.mainSpot.service.SpotListQueryUserCase;
import capstone.jejuTourrecommend.spot.presentation.request.MainPageForm;
import capstone.jejuTourrecommend.spot.presentation.response.ResultSpotListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotListFacade {

    private final SpotListCommandUseCase spotListCommandUseCase;
    private final SpotListQueryUserCase spotListQueryUserCase;

    public ResultSpotListDto searchSpotListContains(Long memberId, String spotName, Pageable pageable) {
        return spotListQueryUserCase.searchSpotListContains(memberId, spotName, pageable);
    }

    public ResultSpotListDto getUserSpotList(MainPageForm mainPageForm, Long memberId, Pageable pageable) {

        List locationList = findLocation(mainPageForm);

        Category category = findCategory(mainPageForm);

        //가중치 존재 유무
        return isPriorityExist(mainPageForm, pageable, locationList, category, memberId);

    }


    public ResultSpotListDto getSpotListWithoutPriority(Pageable pageable, List locationList, Category category, Long memberId) {
        return spotListQueryUserCase.getSpotListWithoutPriority(pageable, locationList, category, memberId);
    }

    public ResultSpotListDto getSpotListWithPriority(Pageable pageable, List locationList, Long memberId, UserWeightDto userWeightDto) {
        return spotListCommandUseCase.getSpotListWithPriority(pageable, locationList, memberId, userWeightDto);
    }

    private ResultSpotListDto isPriorityExist(MainPageForm mainPageForm, Pageable pageable, List locationList, Category category, Long memberId) {

        ResultSpotListDto result;
        if (mainPageForm.getUserWeight() == null ||  //readOnly
                mainPageForm.getUserWeight().get("viewWeight").doubleValue() == 0 &&
                        mainPageForm.getUserWeight().get("priceWeight").doubleValue() == 0 &&
                        mainPageForm.getUserWeight().get("facilityWeight").doubleValue() == 0 &&
                        mainPageForm.getUserWeight().get("surroundWeight").doubleValue() == 0
        ) {
            result = getSpotListWithoutPriority(pageable, locationList, category, memberId);

        } else {//사용자가 가중치를 넣은 경우 //readOnly X
            UserWeightDto userWeightDto = new UserWeightDto(//Todo: 업데이트
                    mainPageForm.getUserWeight().get("viewWeight").doubleValue(),
                    mainPageForm.getUserWeight().get("priceWeight").doubleValue(),
                    mainPageForm.getUserWeight().get("facilityWeight").doubleValue(),
                    mainPageForm.getUserWeight().get("surroundWeight").doubleValue()
            );

            result = getSpotListWithPriority(pageable, locationList, memberId, userWeightDto);

        }
        return result;
    }


    public Category findCategory(MainPageForm mainPageForm) {
        log.info("mainPageCategory = {} ", mainPageForm.getCategory());
        Category category = null;

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

    public List findLocation(MainPageForm mainPageForm) {
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





}
