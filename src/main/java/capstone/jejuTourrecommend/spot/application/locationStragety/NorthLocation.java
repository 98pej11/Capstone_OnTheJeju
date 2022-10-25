package capstone.jejuTourrecommend.spot.application.locationStragety;

import java.util.Arrays;
import java.util.List;

import capstone.jejuTourrecommend.spot.domain.mainSpot.Location;

public class NorthLocation implements LocationStrategy {
	@Override
	public List<Location> getLocation() {

		List<Location> northList = Arrays.asList(Location.Aewol_eup, Location.Jeju_si, Location.Jocheon_eup,
			Location.Gujwa_eup, Location.Udo_myeon);
		return northList;
	}
}
