package capstone.jejuTourrecommend.spot.presentation.response;

import java.util.List;

import capstone.jejuTourrecommend.common.metaDataBuilder.MetaDataDetail;
import lombok.Data;

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
