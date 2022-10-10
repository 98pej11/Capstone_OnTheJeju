package capstone.jejuTourrecommend.web.controller.metaData;

import lombok.Data;

@Data
public class MetaDataDetail {

    private int id;
    private String name;

    public MetaDataDetail(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
