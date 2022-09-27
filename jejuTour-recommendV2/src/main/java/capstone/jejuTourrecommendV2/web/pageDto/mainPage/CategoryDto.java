package capstone.jejuTourrecommendV2.web.pageDto.mainPage;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDto {


    private List list = new ArrayList();

    public CategoryDto(List list) {
        this.list = list;
    }
}






















