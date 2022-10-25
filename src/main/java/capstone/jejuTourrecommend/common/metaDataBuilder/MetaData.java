package capstone.jejuTourrecommend.common.metaDataBuilder;

import java.util.List;

import lombok.Data;

@Data
public class MetaData {

	private List<MetaDataDetail> metaDataList;

	public MetaData(List<MetaDataDetail> metaDataList) {
		this.metaDataList = metaDataList;
	}
}
