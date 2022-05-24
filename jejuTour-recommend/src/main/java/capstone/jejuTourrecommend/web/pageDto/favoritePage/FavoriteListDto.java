package capstone.jejuTourrecommend.web.pageDto.favoritePage;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class FavoriteListDto {

    private Long status;
    private boolean success;
    private String message;

    private Page<FavoriteDto> favoriteDtoPage;

    public FavoriteListDto(Long status, boolean success, String message,
                           Page<FavoriteDto> favoriteDtoPage) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.favoriteDtoPage = favoriteDtoPage;
    }
}









