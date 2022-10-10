package capstone.jejuTourrecommend.spotList.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RegionDto {

    private List list = new ArrayList();

    public RegionDto(List list) {
        this.list = list;
    }
}
