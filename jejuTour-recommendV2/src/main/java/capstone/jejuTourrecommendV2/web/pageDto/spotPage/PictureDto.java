package capstone.jejuTourrecommendV2.web.pageDto.spotPage;

import capstone.jejuTourrecommendV2.domain.Picture;
import lombok.Data;

@Data
public class PictureDto {

    //        "picture": {
//            "id": 1,
//                    "urls":
//						[
//            "url": "http//~~",
//								...
//						]
//        },

    private Long id;
    private String url;

    public PictureDto(Picture picture) {
        this.id = picture.getId();
        this.url = picture.getUrl();
    }
}












