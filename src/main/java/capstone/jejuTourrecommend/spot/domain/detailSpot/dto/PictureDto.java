package capstone.jejuTourrecommend.spot.domain.detailSpot.dto;

import capstone.jejuTourrecommend.spot.domain.detailSpot.Picture;
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












