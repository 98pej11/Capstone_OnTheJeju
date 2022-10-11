package capstone.jejuTourrecommend.spot.application.locationStragety;

import capstone.jejuTourrecommend.spot.domain.mainSpot.Location;

import java.util.Arrays;
import java.util.List;

public class EastLocation implements LocationStrategy{
    @Override
    public List<Location> getLocation() {

        List<Location> eastList = Arrays.asList(Location.Namwon_eup, Location.Pyoseon_myeon, Location.Seongsan_eup);
        return eastList;
    }
}
