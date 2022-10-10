package capstone.jejuTourrecommend.spot.domain.mainSpot.service.locationStragety;

import capstone.jejuTourrecommend.spot.domain.mainSpot.Location;

import java.util.Arrays;
import java.util.List;

public class DefaultLocation implements LocationStrategy{
    @Override
    public List<Location> getLocation() {

        List<Location> DefaultList = Arrays.asList(Location.Jeju_si, Location.Aewol_eup, Location.Hallim_eup,
                Location.Hangyeong_myeon, Location.Jocheon_eup, Location.Gujwa_eup,
                Location.Daejeong_eup, Location.Andeok_myeon, Location.Seogwipo_si,
                Location.Namwon_eup, Location.Pyoseon_myeon, Location.Seongsan_eup, Location.Udo_myeon
        );

        return DefaultList;
    }
}
