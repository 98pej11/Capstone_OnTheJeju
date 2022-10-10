package capstone.jejuTourrecommend.spotList.dto;

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






















