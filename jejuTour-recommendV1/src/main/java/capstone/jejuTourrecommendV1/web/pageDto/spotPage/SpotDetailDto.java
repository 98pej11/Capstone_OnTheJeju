package capstone.jejuTourrecommendV1.web.pageDto.spotPage;

import lombok.Data;

import java.util.List;

@Data
public class SpotDetailDto {

    private SpotDto spotDto;
    private ScoreDto scoreDto;
    private List<PictureDto> pictureDto;
    private Boolean isFavoriteSpot;

    public SpotDetailDto(SpotDto spotDto, ScoreDto scoreDto,
                         List<PictureDto> pictureDto,Boolean isFavoriteSpot) {
        this.spotDto = spotDto;
        this.scoreDto = scoreDto;
        this.pictureDto = pictureDto;
        this.isFavoriteSpot = isFavoriteSpot;
    }
}
