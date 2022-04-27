package capstone.jejuTourrecommend.repository;

import capstone.jejuTourrecommend.domain.Category;
import capstone.jejuTourrecommend.domain.Location;
import capstone.jejuTourrecommend.domain.Spot;
import capstone.jejuTourrecommend.web.mainPage.SpotLocationDto;
import capstone.jejuTourrecommend.web.mainPage.UserWeightDto;
import capstone.jejuTourrecommend.web.spotPage.ScoreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface SpotRepositoryCustom {

    Page<SpotLocationDto> searchSpotByLocationAndCategory(Location location, Category category, Pageable pageable);

    Page<SpotLocationDto> searchSpotByUserPriority(Long memberId, Location location, UserWeightDto userWeightDto, Pageable pageable);

    //Page<SpotDetailDto> searchSpotDetail(String spotName);

    ScoreDto searchScore(Spot spot);

}
