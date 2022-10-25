package capstone.jejuTourrecommend.wishList.domain.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FavoriteListDto {

	private Long favoriteId;
	private String favoriteName;

	private List<PictureUrlDto> PictureUrlDtoList;

	public FavoriteListDto(Long favoriteId, String favoriteName) {
		this.favoriteId = favoriteId;
		this.favoriteName = favoriteName;
		PictureUrlDtoList = new ArrayList<>();
	}

}
