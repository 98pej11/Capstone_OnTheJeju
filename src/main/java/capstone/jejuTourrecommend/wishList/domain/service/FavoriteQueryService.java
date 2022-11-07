package capstone.jejuTourrecommend.wishList.domain.service;

import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.repository.PictureRepository;
import capstone.jejuTourrecommend.spot.domain.detailSpot.repository.ScoreRepository;
import capstone.jejuTourrecommend.spot.domain.mainSpot.Category;
import capstone.jejuTourrecommend.spot.domain.mainSpot.Location;
import capstone.jejuTourrecommend.spot.domain.mainSpot.repository.SpotRepository;
import capstone.jejuTourrecommend.wishList.domain.Favorite;
import capstone.jejuTourrecommend.wishList.domain.dto.*;
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
import java.util.Map;
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
	private final ScoreRepository scoreRepository;

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


	public List<List<RouteSpotListDto>> recommendSpotList(Long favoriteId, RouteForm routeForm) {
		List<Long> spotIdList = favoriteSpotRepository.getSpotIdList(favoriteId, routeForm.getSpotIdList());
		ScoreSumDto scoreSumDto = scoreRepository.getScoreSumDto(spotIdList);
		Category highestScoreCategory = getHighestScoreCategory(scoreSumDto);
		List<Location> distinctLocationBySpotIdList = spotRepository.findDistinctLocationBySpotIdList(spotIdList);
		return getRouteSpotDtoList(highestScoreCategory, distinctLocationBySpotIdList);
	}

	private List<List<RouteSpotListDto>> getRouteSpotDtoList(Category highestScoreCategory, List<Location> distinctLocationBySpotIdList) {
		List<List<RouteSpotListDto>> list = new ArrayList<>();
		for (Location location : distinctLocationBySpotIdList) {
			List<RouteSpotListDto> routeSpotListDtos = spotRepository.getRouteSpotListDtos(location, highestScoreCategory);
			List<Long> spotIdList = routeSpotListDtos.stream().map(s -> s.getSpotId()).collect(Collectors.toList());
			List<PictureUrlDto> pictureUrlDtos = pictureRepository.findPictureUrlDtos(spotIdList, spotIdList.size());

			Map<Long, List<PictureUrlDto>> groupBySpotId = pictureUrlDtos.stream()
				.collect(Collectors.groupingBy(p -> p.getSpotId()));
			if (groupBySpotId.isEmpty()) {	//사진이 없는 경우
				continue;
			}
			routeSpotListDtos.forEach(sl -> sl.setUrl(groupBySpotId.get(sl.getSpotId()).get(0).getPictureUrl()));
			list.add(routeSpotListDtos);
		}
		return list;
	}

	public void deleteFavoriteList(Long favoriteId) {
		favoriteRepository.findOptionById(favoriteId)
			.orElseThrow(() -> new UserException("올바르지 않는 favoriteId 입니다"));
		favoriteSpotRepository.deleteAllByFavoriteId(favoriteId);
		favoriteRepository.deleteById(favoriteId);
	}

	private Category getHighestScoreCategory(ScoreSumDto scoreSumDto) {
		Double[] score = new Double[4];
		score[0] = scoreSumDto.getViewScoreSum();
		log.info("score[0] = {}", score[0]);
		score[1] = scoreSumDto.getPriceScoreSum();
		score[2] = scoreSumDto.getFacilityScoreSum();
		score[3] = scoreSumDto.getSurroundScoreSum();

		Double max = score[0];
		int j = 0;
		for (int i = 0; i < 4; i++) {
			if (max < score[i]) {
				j = i;
				max = score[i];
			}
		}
		if (j == 0)
			return Category.VIEW;
		else if (j == 1)
			return Category.PRICE;
		else if (j == 2)
			return Category.FACILITY;
		else
			return Category.SURROUND;
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

}
