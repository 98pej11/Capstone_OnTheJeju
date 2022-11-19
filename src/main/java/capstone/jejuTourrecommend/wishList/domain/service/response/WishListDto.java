package capstone.jejuTourrecommend.wishList.domain.service.response;

import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class WhishListDto {

	private Long status;
	private boolean success;
	private String message;

	private Page<FavoriteListDto> favoriteListDtos;

	public WhishListDto(Long status, boolean success, String message,
						Page<FavoriteListDto> favoriteListDtos) {
		this.status = status;
		this.success = success;
		this.message = message;
		this.favoriteListDtos = favoriteListDtos;
	}
}
