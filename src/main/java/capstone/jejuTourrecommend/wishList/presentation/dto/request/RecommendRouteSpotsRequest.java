package capstone.jejuTourrecommend.wishList.presentation.dto.request;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecommendRouteSpotsRequest {

	private List<Long> spotIdList;

}



