package capstone.jejuTourrecommend.common.metaDataBuilder;

public class MetaDataDirector {

	int id = 1;
	private MetaDataBuilder metaDataBuilder;

	public MetaDataDirector(MetaDataBuilder metaDataBuilder) {
		this.metaDataBuilder = metaDataBuilder;
	}

	public MetaData categoryMetaData() {
		return metaDataBuilder
			.addMetaData(id++, "전체")
			.addMetaData(id++, "뷰")
			.addMetaData(id++, "가격")
			.addMetaData(id++, "편의시설")
			.addMetaData(id++, "서비스")
			.getMetaDataDummy();
	}

	public MetaData locationMetaData() {
		return metaDataBuilder
			.addMetaData(id++, "전체")
			.addMetaData(id++, "북부")
			.addMetaData(id++, "남부")
			.addMetaData(id++, "서부")
			.addMetaData(id++, "동부")
			.getMetaDataDummy();
	}

}
