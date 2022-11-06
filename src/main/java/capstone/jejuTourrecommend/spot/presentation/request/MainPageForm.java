package capstone.jejuTourrecommend.spot.presentation.request;

import lombok.Data;

import java.util.Map;

@Data
public class MainPageForm {

	private String location;
	private String category;
	private Map<String, Double> userWeight;

}









