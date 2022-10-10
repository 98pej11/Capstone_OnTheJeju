package capstone.jejuTourrecommend.web.pageDto.mainPage;

import capstone.jejuTourrecommend.web.controller.metaData.MetaDataDetail;
import lombok.Data;

import java.util.List;

@Data
public class SpotListMetaDataOp {

    private Long status;
    private boolean success;
    //private CategoryDto categoryDto;
    //private RegionDto regionDto;
    private List<MetaDataDetail> MetaDataDetailList;

    public SpotListMetaDataOp(Long status, boolean success, List<MetaDataDetail> metaDataDetailList) {
        this.status = status;
        this.success = success;
        MetaDataDetailList = metaDataDetailList;
    }
}
