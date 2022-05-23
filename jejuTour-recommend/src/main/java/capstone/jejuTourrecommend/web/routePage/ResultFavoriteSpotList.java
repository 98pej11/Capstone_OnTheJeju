package capstone.jejuTourrecommend.web.routePage;


import capstone.jejuTourrecommend.web.mainPage.SpotListDto;
import lombok.Data;

import java.util.List;

@Data
public class ResultFavoriteSpotList {

    private Long status;
    private boolean success;
    private String message;
    private List<SpotListDto> spotListDtoList;

    public ResultFavoriteSpotList(Long status, boolean success, String message, List<SpotListDto> spotListDtoList) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.spotListDtoList = spotListDtoList;
    }
}





