package capstone.jejuTourrecommend.wishList.domain.service;

import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.repository.PictureRepository;
import capstone.jejuTourrecommend.spot.domain.mainSpot.repository.SpotRepository;
import capstone.jejuTourrecommend.wishList.domain.Favorite;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import capstone.jejuTourrecommend.wishList.domain.dto.PictureUrlDto;
import capstone.jejuTourrecommend.wishList.domain.dto.SpotListDtoByFavoriteSpot;
import capstone.jejuTourrecommend.wishList.domain.repository.FavoriteRepository;
import capstone.jejuTourrecommend.wishList.domain.repository.FavoriteSpotRepository;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.RouteForm;
import capstone.jejuTourrecommend.wishList.presentation.dto.response.ResultFavoriteSpotList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteQueryService implements FavoriteQueryUseCase {

	private final FavoriteRepository favoriteRepository;
	private final FavoriteSpotRepository favoriteSpotRepository;
	private final PictureRepository pictureRepository;
	private final SpotRepository spotRepository;

	/**
	 * 사용자의 위시리스트 목록 "폼"+ 위시리스트 페이지 보여주기 + "여러 사진"도 줌
	 * 폼 api 사용할때는 사진 여러장 중 하나만 고르면 되니깐 ㄱㅊ
	 * readonly
	 * @param memberId
	 * @param pageable
	 * @return
	 */
	public Page<FavoriteListDto> getFavoriteList(Long memberId, Pageable pageable) {

		Page<FavoriteListDto> favoriteListDtos = favoriteRepository.getFavoriteList(memberId, pageable);
		postPictureUrlOnFavoriteList(favoriteListDtos.getContent());

		return favoriteListDtos;

	}

	private void postPictureUrlOnFavoriteList(List<FavoriteListDto> favoriteListDtos) {
		List<Long> favoriteIdList = favoriteListDtos.stream().map(o -> o.getFavoriteId()).collect(Collectors.toList());

		for (Long favoriteId : favoriteIdList) {
			List<Long> spotIdList = favoriteSpotRepository.findSpotIdByFavoriteId(favoriteId);
			List<PictureUrlDto> pictureUrlDtos = pictureRepository.findPictureUrlDtos(spotIdList, 3);
			favoriteListDtos.stream()
				.filter(i -> i.getFavoriteId()==favoriteId)
				.forEach(sl -> sl.setPictureUrlDtoList(pictureUrlDtos));
		}
	}


	public List recommendSpotList(Long favoriteId, RouteForm routeForm) {

		List list = favoriteSpotRepository.recommendSpotList(favoriteId, routeForm);

		return list;

	}

	public ResultFavoriteSpotList favoriteSpotList(Long favoriteId) {

		Favorite favorite = favoriteRepository.findOptionById(favoriteId)
			.orElseThrow(() -> new UserException("올바르지 않은 favoriteId 입니다"));

		List<Long> spotIdList = favoriteSpotRepository.findSpotIdByFavoriteId(favoriteId);
		List<Spot> spotBySpotIdList = spotRepository.findSpotFetchJoinBySpotIdList(spotIdList);
		List<SpotListDtoByFavoriteSpot> spotListDtosByFavoriteSpot = postPictureUrlOnSpotList(spotBySpotIdList);

		return new ResultFavoriteSpotList(200l, true, "성공", favorite.getName(), spotListDtosByFavoriteSpot);

	}

	private List<SpotListDtoByFavoriteSpot> postPictureUrlOnSpotList(List<Spot> spotBySpotIdList) {
		List<SpotListDtoByFavoriteSpot> spotListDtosByFavoriteSpot = new ArrayList<>();
		spotBySpotIdList.stream().forEach(spot -> {
			if (!spot.getPictures().isEmpty()) {
				spotListDtosByFavoriteSpot.add(
					new SpotListDtoByFavoriteSpot(spot.getId(), spot.getName(), spot.getAddress(),
						spot.getDescription(),
						spot.getPictures().get(0).getUrl()));
			} else {
				spotListDtosByFavoriteSpot.add(
					new SpotListDtoByFavoriteSpot(spot.getId(), spot.getName(), spot.getAddress(),
						spot.getDescription(),
						"Empty"));
			}
		});
		return spotListDtosByFavoriteSpot;
	}

	/**
	 * 위시 리스트 삭제하기
	 * 해당 위시리스트 정보 필요
	 *
	 * @param favoriteId
	 */
	public void deleteFavoriteList(Long favoriteId) {

		favoriteRepository.findOptionById(favoriteId)
			.orElseThrow(() -> new UserException("올바르지 않는 favoriteId 입니다"));

		favoriteSpotRepository.deleteAllByFavoriteId(favoriteId);

		favoriteRepository.deleteById(favoriteId);

	}

}
