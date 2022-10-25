package capstone.jejuTourrecommend.spot.domain.mainSpot.dto;

import javax.persistence.Lob;

import lombok.Data;

@Data
public class PictureDetailDto {

	private Long pictureId;

	@Lob
	private String url;

	private Long spotId;

	public PictureDetailDto(Long pictureId, String url, Long spotId) {
		this.pictureId = pictureId;
		this.url = url;
		this.spotId = spotId;
	}
}
