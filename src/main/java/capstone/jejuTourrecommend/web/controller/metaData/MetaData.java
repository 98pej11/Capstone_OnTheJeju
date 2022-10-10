package capstone.jejuTourrecommend.web.controller.metaData;

import lombok.Data;

import java.util.List;

@Data
public class MetaData {

    private List<MetaDataDetail> metaDataList;

    public MetaData(List<MetaDataDetail> metaDataList) {
        this.metaDataList = metaDataList;
    }
}
