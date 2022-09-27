package capstone.jejuTourrecommendV2.web.pageDto.favoritePage;


import lombok.Data;

@Data
public class FavoriteDto {

    private Long id;
    private String name;

    public FavoriteDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
