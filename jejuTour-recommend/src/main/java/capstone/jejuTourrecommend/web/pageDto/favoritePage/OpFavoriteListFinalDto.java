package capstone.jejuTourrecommend.web.pageDto.favoritePage;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class OpFavoriteListFinalDto {

    private Long status;
    private boolean success;
    private String message;

    private Page<FavoriteListDto> OptimizationFavoriteListDtos;

    public OpFavoriteListFinalDto(Long status, boolean success, String message,
                                Page<FavoriteListDto> optimizationFavoriteListDtos) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.OptimizationFavoriteListDtos = optimizationFavoriteListDtos;
    }
}
