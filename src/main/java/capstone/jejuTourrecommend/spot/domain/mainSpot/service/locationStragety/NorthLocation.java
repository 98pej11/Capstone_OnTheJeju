package capstone.jejuTourrecommend.spot.domain.mainSpot.service.locationStragety;

import capstone.jejuTourrecommend.spot.domain.mainSpot.Location;

import java.util.Arrays;
import java.util.List;

public class NorthLocation implements LocationStrategy {
    @Override
    public List<Location> getLocation() {

        List<Location> northList = Arrays.asList(Location.Aewol_eup, Location.Jeju_si, Location.Jocheon_eup,
                Location.Gujwa_eup, Location.Udo_myeon);
        return northList;
    }
}
