package capstone.jejuTourrecommendV1.web.pageDto.spotPage;

import capstone.jejuTourrecommendV1.domain.Picture;
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












