package capstone.jejuTourrecommend.web.mainPage;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Data
public class ResultSpotListDto {

    private Long status;
    private boolean success;
    private String message;
    private Page<SpotLocationDto> data;

    public ResultSpotListDto(Long status, boolean success, String message, Page<SpotLocationDto> data) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
