package capstone.jejuTourrecommend.web.spotPage;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class SpotDetailDto {

    private SpotDto spotDto;
    private ScoreDto scoreDto;
    private List<PictureDto> pictureDto;
    private Page<ReviewDto> reviewDto;
    private Double userScore;

    public SpotDetailDto(SpotDto spotDto, ScoreDto scoreDto,
                         List<PictureDto> pictureDto, Page<ReviewDto> reviewDto,
                         Double userScore) {
        this.spotDto = spotDto;
        this.scoreDto = scoreDto;
        this.pictureDto = pictureDto;
        this.reviewDto = reviewDto;
        this.userScore = userScore;
    }
}
