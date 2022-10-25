package capstone.jejuTourrecommend.wishList.domain.dto;

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
