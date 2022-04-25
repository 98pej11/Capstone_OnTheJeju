package capstone.jejuTourrecommend.repository;

import capstone.jejuTourrecommend.domain.Location;
import capstone.jejuTourrecommend.domain.Spot;
import capstone.jejuTourrecommend.web.mainPage.SpotLocationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SpotRepositoryCustom {

    List<SpotLocationDto> findSpotByLocation(Location location);

}
