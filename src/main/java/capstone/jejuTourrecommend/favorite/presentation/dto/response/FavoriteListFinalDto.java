package capstone.jejuTourrecommend.favorite.presentation.dto.response;

import capstone.jejuTourrecommend.favorite.domain.dto.FavoriteListDto;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class FavoriteListFinalDto {

    private Long status;
    private boolean success;
    private String message;

    private Page<FavoriteListDto> favoriteListDtos;

    public FavoriteListFinalDto(Long status, boolean success, String message,
                                Page<FavoriteListDto> favoriteListDtos) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.favoriteListDtos = favoriteListDtos;
    }
}
