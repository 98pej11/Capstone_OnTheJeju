package capstone.jejuTourrecommendV1.web.pageDto.mainPage;


import lombok.Data;

import java.util.List;

@Data
public class OptimizationSpotListDto {

    private Long spotId;
    private String spotName;
    private String spotAddress;
    private String spotDescription;

    private List<PictureDetailDto> pictureDetailDtoList;

    private boolean isFavorite;




    public OptimizationSpotListDto(Long spotId, String spotName, String spotAddress, String spotDescription) {
        this.spotId = spotId;
        this.spotName = spotName;
        this.spotAddress = spotAddress;
        this.spotDescription = spotDescription;

    }
}
