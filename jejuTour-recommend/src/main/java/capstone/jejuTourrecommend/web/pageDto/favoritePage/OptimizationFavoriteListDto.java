package capstone.jejuTourrecommend.web.pageDto.favoritePage;

import capstone.jejuTourrecommend.web.pageDto.mainPage.PictureDetailDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OptimizationFavoriteListDto {

    private Long favoriteId;
    private String favoriteName;

    private List<List<PictureDetailDto>> pictureDetailDtoListBySpotId;


    public OptimizationFavoriteListDto(Long favoriteId, String favoriteName) {
        this.favoriteId = favoriteId;
        this.favoriteName = favoriteName;
        pictureDetailDtoListBySpotId = new ArrayList<>();
    }
}
