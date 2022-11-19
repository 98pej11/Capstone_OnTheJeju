package capstone.jejuTourrecommend.spot.presentation.response;

import capstone.jejuTourrecommend.common.metaDataBuilder.MetaDataDetail;
import lombok.Data;

import java.util.List;

@Data
public class SpotsMetaDataOp {

	private Long status;
	private boolean success;
	private List<MetaDataDetail> MetaDataDetailList;

	public SpotsMetaDataOp(Long status, boolean success, List<MetaDataDetail> metaDataDetailList) {
		this.status = status;
		this.success = success;
		this.MetaDataDetailList = metaDataDetailList;
	}
}
