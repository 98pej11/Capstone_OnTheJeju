package capstone.jejuTourrecommend.web.pageDto.mainPage;

import lombok.Data;

import javax.persistence.Lob;


@Data
public class PictureDetailDto {


    private Long pictureId;

    @Lob
    private String url;

    private Long spotId;

    public PictureDetailDto(Long pictureId, String url, Long spotId) {
        this.pictureId = pictureId;
        this.url = url;
        this.spotId = spotId;
    }
}
