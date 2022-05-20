package capstone.jejuTourrecommend.web.mainPage;

import capstone.jejuTourrecommend.domain.Category;
import capstone.jejuTourrecommend.domain.Location;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MainPageForm {

    //private Double id;//이거 프런드와 변수명 맞아야 함

    private String location;
    private String category;

    private UserWeightDto userWeightDto;

    //private Map<String, Double> userWeight;



}









