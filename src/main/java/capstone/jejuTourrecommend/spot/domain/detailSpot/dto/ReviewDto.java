package capstone.jejuTourrecommend.spot.domain.detailSpot.dto;

import capstone.jejuTourrecommend.domain.Review;
import lombok.Data;

@Data
public class ReviewDto {


//        "review": {
//            "id": 2,
//                    "contents": [
//            content: "주변시설이 좋아요",
//							...
//						]
//        }

    private Long id;
    private String content;

    //@QueryProjection
    public ReviewDto(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public ReviewDto(Review review) {
        this.id = review.getId();
        this.content = review.getContent();
    }
}








