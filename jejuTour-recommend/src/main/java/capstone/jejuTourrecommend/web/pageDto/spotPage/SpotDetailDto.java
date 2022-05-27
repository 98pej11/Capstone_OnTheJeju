package capstone.jejuTourrecommend.web.pageDto.spotPage;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class SpotDetailDto {

    private SpotDto spotDto;
    private ScoreDto scoreDto;
    private List<PictureDto> pictureDto;

    public SpotDetailDto(SpotDto spotDto, ScoreDto scoreDto,
                         List<PictureDto> pictureDto) {
        this.spotDto = spotDto;
        this.scoreDto = scoreDto;
        this.pictureDto = pictureDto;
    }
}
