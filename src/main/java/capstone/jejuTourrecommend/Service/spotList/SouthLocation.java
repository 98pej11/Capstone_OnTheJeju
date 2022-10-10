package capstone.jejuTourrecommend.Service.spotList;

import capstone.jejuTourrecommend.domain.Location;

import java.util.Arrays;
import java.util.List;

public class SouthLocation implements LocationStrategy{
    @Override
    public List<Location> getLocation() {

        List<Location> southList = Arrays.asList(Location.Seogwipo_si);
        return southList;
    }
}
