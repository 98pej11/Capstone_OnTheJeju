package capstone.jejuTourrecommend.web.spotPage;

import lombok.Data;

import java.util.List;

@Data
public class ReivewDto {


//        "review": {
//            "id": 2,
//                    "contents": [
//            content: "주변시설이 좋아요",
//							...
//						]
//        }

    private Long id;
    private List<String> contents;

}








