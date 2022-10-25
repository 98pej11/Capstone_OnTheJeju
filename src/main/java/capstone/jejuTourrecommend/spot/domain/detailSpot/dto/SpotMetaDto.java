package capstone.jejuTourrecommend.spot.domain.detailSpot.dto;

import java.util.List;

import lombok.Data;

@Data
public class SpotMetaDto {

	private Long status;
	private boolean success;
	//private CategoryDto categoryDto;
	private List categoryDummy;

	public SpotMetaDto(Long status, boolean success, List categoryDummy) {
		this.status = status;
		this.success = success;
		this.categoryDummy = categoryDummy;
	}
}
