package capstone.jejuTourrecommend.wishList.domain.service.response;

import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WishListDto {

	private Page<FavoriteListDto> favoriteListDtos;

	public static WishListDto from(Page<FavoriteListDto> favoriteListDtos) {
		return WishListDto.builder()
			.favoriteListDtos(favoriteListDtos)
			.build();
	}

}
