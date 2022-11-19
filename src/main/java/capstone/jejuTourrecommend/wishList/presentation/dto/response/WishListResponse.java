package capstone.jejuTourrecommend.wishList.presentation.dto.response;

import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class WhishListResponse {

	private Long status;
	private boolean success;
	private String message;

	private Page<FavoriteListDto> favoriteListDtos;

	public WhishListResponse(Long status, boolean success, String message,
							 Page<FavoriteListDto> favoriteListDtos) {
		this.status = status;
		this.success = success;
		this.message = message;
		this.favoriteListDtos = favoriteListDtos;
	}
}
