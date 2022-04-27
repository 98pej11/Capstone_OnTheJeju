package capstone.jejuTourrecommend.web.spotPage;

import lombok.Data;

@Data
public class ScoreDto {

    //        "score": {
//            "viewScore": 11.2, //뷰 점수
//                    "priceScore": 14.2, //가격 점수
//                    "facilityScore": 13.3, //시설 점수
//                    "surroundScore": 12.3, //주변 점수
//
//                    "viewRank": 43.2, //뷰 순위
//                    "priceRank": 12.2, //가격 순위
//                    "facilityRank": 1.1, //시설 순위
//                    "surroundRank": 14.2, //주변 순위
//        },

    private Long id;

    private Double viewScore;
    private Double priceScore;
    private Double facilityScore;
    private Double surroundScore;

    private Double viewRank;
    private Double priceRank;
    private Double facilityRank;
    private Double surroundRank;


}
