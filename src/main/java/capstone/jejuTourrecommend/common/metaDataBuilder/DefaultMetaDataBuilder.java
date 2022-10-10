package capstone.jejuTourrecommend.common.metaDataBuilder;

import java.util.ArrayList;
import java.util.List;

public class DefaultMetaDataBuilder implements MetaDataBuilder{

    private List<MetaDataDetail> metaDataDetailList;

    @Override
    public MetaDataBuilder addMetaData(int id, String name) {

        if (this.metaDataDetailList == null) {
            this.metaDataDetailList = new ArrayList<>();
        }

        MetaDataDetail metaDataDetail = new MetaDataDetail(id, name);

        this.metaDataDetailList.add(metaDataDetail);

        return this;
    }

    @Override
    public MetaData getMetaDataDummy() {
        return new MetaData(metaDataDetailList);
    }


}
