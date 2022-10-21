package capstone.jejuTourrecommend.spot.domain.mainSpot.dto;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SpotListDto {

    private Long spotId;
    private String spotName;
    private String spotAddress;
    private String spotDescription;

    private List<PictureDetailDto> pictureDetailDtoList;

    private boolean isFavorite;




    public SpotListDto(Long spotId, String spotName, String spotAddress, String spotDescription) {
        this.spotId = spotId;
        this.spotName = spotName;
        this.spotAddress = spotAddress;
        this.spotDescription = spotDescription;
        pictureDetailDtoList = new ArrayList<>();
    }
}
