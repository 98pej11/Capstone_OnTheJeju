package capstone.jejuTourrecommend.spot.domain.mainSpot.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class RegionDto {

	private List list = new ArrayList();

	public RegionDto(List list) {
		this.list = list;
	}
}
