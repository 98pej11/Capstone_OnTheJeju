package capstone.jejuTourrecommend.web.pageDto.mainPage;

import lombok.Data;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Lob;

@BatchSize(size = 100)
@Data
public class PictureDto {


    private Long pictureId;

    @Lob
    private String url;

    private Long spotId;

    public PictureDto(Long pictureId, String url, Long spotId) {
        this.pictureId = pictureId;
        this.url = url;
        this.spotId = spotId;
    }
}
