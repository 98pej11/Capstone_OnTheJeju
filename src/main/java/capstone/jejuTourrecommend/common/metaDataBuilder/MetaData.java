package capstone.jejuTourrecommend.common.metaDataBuilder;

import lombok.Data;

import java.util.List;

@Data
public class MetaData {

    private List<MetaDataDetail> metaDataList;

    public MetaData(List<MetaDataDetail> metaDataList) {
        this.metaDataList = metaDataList;
    }
}
