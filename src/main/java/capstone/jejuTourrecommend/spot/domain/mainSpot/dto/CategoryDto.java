package capstone.jejuTourrecommend.spot.domain.mainSpot.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CategoryDto {

	private List list = new ArrayList();

	public CategoryDto(List list) {
		this.list = list;
	}
}






















