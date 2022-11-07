package capstone.jejuTourrecommend.spot.application.locationStragety;

import capstone.jejuTourrecommend.spot.domain.mainSpot.Location;

import java.util.List;

/**
 * 북 : 애월읍,제주시,조천읍,구좌읍,우도면
 * 동 : 남원읍, 표선면, 성산읍
 * 서 : 한림읍, 한경면, 대정읍, 안덕면
 * 남 : 서귀포시
 */
public interface LocationStrategy {

	List<Location> getLocation();

}
