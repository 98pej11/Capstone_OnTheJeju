package capstone.jejuTourrecommend.spot.domain.mainSpot.service;

import capstone.jejuTourrecommend.spot.domain.mainSpot.Category;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.UserWeightDto;
import capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot.SpotQuerydslRepository;
import capstone.jejuTourrecommend.spot.presentation.response.ResultSpotListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpotListService implements SpotListQueryUserCase, SpotListCommandUseCase{


    private final SpotQuerydslRepository spotQuerydslRepository;

    @Override
    public ResultSpotListDto searchSpotListContains(Long memberId, String spotName, Pageable pageable) {

        Page<SpotListDto> spotListDtos = spotQuerydslRepository.searchBySpotNameContains(memberId, spotName, pageable);

        return new ResultSpotListDto(200l, true, "성공", spotListDtos);

    }

    @Transactional
    @Override
    public ResultSpotListDto getSpotListWithPriority(Pageable pageable, List locationList, Long memberId, UserWeightDto userWeightDto) {
        Page<SpotListDto> result = spotQuerydslRepository.searchSpotByUserPriority(
                memberId, locationList, userWeightDto, pageable);
        return new ResultSpotListDto(200l, true, "성공", result);
    }

    @Override
    public ResultSpotListDto getSpotListWithoutPriority(Pageable pageable, List locationList, Category category, Long memberId) {
        Page<SpotListDto> result = spotQuerydslRepository.searchSpotByLocationAndCategory(memberId,
                locationList, category, pageable);
        return new ResultSpotListDto(200l, true, "성공", result);
    }



}




