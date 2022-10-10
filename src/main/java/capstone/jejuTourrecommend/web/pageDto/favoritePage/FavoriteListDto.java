package capstone.jejuTourrecommend.web.pageDto.favoritePage;

import capstone.jejuTourrecommend.web.pageDto.mainPage.PictureDetailDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FavoriteListDto {

    private Long favoriteId;
    private String favoriteName;

    private List<List<PictureDetailDto>> pictureDetailDtoListBySpotId;


    public FavoriteListDto(Long favoriteId, String favoriteName) {
        this.favoriteId = favoriteId;
        this.favoriteName = favoriteName;
        pictureDetailDtoListBySpotId = new ArrayList<>();
    }
}
