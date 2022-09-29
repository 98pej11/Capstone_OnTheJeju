package capstone.jejuTourrecommend.web.controller.metaData;

public class MetaDataDirector {

    private MetaDataBuilder metaDataBuilder;

    public MetaDataDirector(MetaDataBuilder metaDataBuilder) {
        this.metaDataBuilder = metaDataBuilder;
    }

    public MetaData categoryMetaData() {
        return metaDataBuilder
                .addMetaData(1,"전체")
                .addMetaData(2,"뷰")
                .addMetaData(3,"가격")
                .addMetaData(4,"편의시설")
                .getMetaDataDummy();
    }

    public MetaData noCaAndLocationMetaData() {
        return metaDataBuilder
                .addMetaData(5,"전체")
                .addMetaData(6,"북부")
                .addMetaData(7,"남부")
                .addMetaData(8,"서부")
                .addMetaData(9,"동부")
                .getMetaDataDummy();
    }

    public MetaData locationMetaData() {
        return metaDataBuilder
                .addMetaData(1,"전체")
                .addMetaData(2,"북부")
                .addMetaData(3,"남부")
                .addMetaData(4,"서부")
                .addMetaData(5,"동부")
                .getMetaDataDummy();
    }



}
