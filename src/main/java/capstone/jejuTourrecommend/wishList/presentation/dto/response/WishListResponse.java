package capstone.jejuTourrecommend.wishList.presentation.dto.response;

import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import capstone.jejuTourrecommend.wishList.domain.service.response.WishListDto;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishListResponse {

	private Long status;
	private boolean success;
	private String message;
	private Page<FavoriteListDto> favoriteListDtos;

	public static WishListResponse from(Long status, boolean success, String message, WishListDto wishListDto) {
		return new WishListResponse(status, success,
			message, wishListDto.getFavoriteListDtos());
	}
}
