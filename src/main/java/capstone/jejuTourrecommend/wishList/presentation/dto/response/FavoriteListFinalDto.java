package capstone.jejuTourrecommend.wishList.presentation.dto.response;

import org.springframework.data.domain.Page;

import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import lombok.Data;

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
