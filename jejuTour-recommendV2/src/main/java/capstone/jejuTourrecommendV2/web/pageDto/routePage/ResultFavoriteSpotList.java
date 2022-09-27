package capstone.jejuTourrecommendV2.web.pageDto.routePage;


import capstone.jejuTourrecommendV2.web.pageDto.favoritePage.SpotListDtoByFavoriteSpot;
import lombok.Data;

import java.util.List;

@Data
public class ResultFavoriteSpotList {

    private Long status;
    private boolean success;
    private String message;
    private String favoriteName;
    private List<SpotListDtoByFavoriteSpot> spotListDtoByFavoriteSpots;

    public ResultFavoriteSpotList(Long status, boolean success, String message, String favoriteName, List<SpotListDtoByFavoriteSpot> spotListDtoByFavoriteSpots) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.favoriteName = favoriteName;
        this.spotListDtoByFavoriteSpots = spotListDtoByFavoriteSpots;
    }
}





