package capstone.jejuTourrecommendV1.web.pageDto.favoritePage;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class FavoriteListDto {

    private Long favoriteId;
    private String favoriteName;
    private String spotURL;

    @QueryProjection
    public FavoriteListDto(Long favoriteId, String favoriteName, String spotURL) {
        this.favoriteId = favoriteId;
        this.favoriteName = favoriteName;
        this.spotURL = spotURL;
    }

    //2개 사용 가능함 ㅋ
    @QueryProjection
    public FavoriteListDto(Long favoriteId, String favoriteName) {
        this.favoriteId = favoriteId;
        this.favoriteName = favoriteName;
    }
}
