package capstone.jejuTourrecommend.spot.domain.detailSpot.dto;

import capstone.jejuTourrecommend.spot.domain.Spot;
import lombok.Data;

@Data
public class SpotDto {

	private Long id;
	private String name;
	private String address;
	private String description;

	public SpotDto(Spot spot) {
		this.id = spot.getId();
		this.name = spot.getName();
		this.address = spot.getAddress();
		this.description = spot.getDescription();
	}
}
