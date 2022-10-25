package capstone.jejuTourrecommend.wishList.domain.dto;

import javax.persistence.Lob;

import lombok.Data;

@Data
public class PictureUrlDto {

	private Long spotId;
	@Lob
	private String pictureUrl;

	public PictureUrlDto(Long spotId, String pictureUrl) {
		this.spotId = spotId;
		this.pictureUrl = pictureUrl;
	}
}
