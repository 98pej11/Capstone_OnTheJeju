package capstone.jejuTourrecommend.spot.domain.mainSpot.service;

import capstone.jejuTourrecommend.spot.domain.detailSpot.repository.PictureRepository;
import capstone.jejuTourrecommend.spot.domain.mainSpot.Category;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.PictureDetailDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.UserWeightDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.repository.MemberSpotRepository;
import capstone.jejuTourrecommend.spot.domain.mainSpot.repository.SpotRepository;
import capstone.jejuTourrecommend.spot.presentation.response.ResultSpotListDto;
import capstone.jejuTourrecommend.wishList.domain.repository.FavoriteSpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpotListService implements SpotListQueryUserCase, SpotListCommandUseCase {

	private final SpotRepository spotRepository;
	private final MemberSpotRepository memberSpotRepository;
	private final PictureRepository pictureRepository;
	private final FavoriteSpotRepository favoriteSpotRepository;

	@Override
	public ResultSpotListDto searchSpotListContains(Long memberId, String spotName, Pageable pageable) {

		Page<SpotListDto> spotListDtos = spotRepository.searchBySpotNameContains(memberId, spotName, pageable);
		postPictureAndBooleanFavoriteOnSpotListDto(spotListDtos, memberId);
		return new ResultSpotListDto(200l, true, "성공", spotListDtos);

	}

	@Transactional//readOnly X
	@Override
	public ResultSpotListDto getSpotListWithPriority(Pageable pageable, List locationList, Long memberId,
		UserWeightDto userWeightDto) {
		memberSpotRepository.updateMemberSpotByPriority(memberId,userWeightDto);
		Page<SpotListDto> spotListDtos = memberSpotRepository.searchSpotByUserPriority(memberId, locationList, pageable);
		postPictureAndBooleanFavoriteOnSpotListDto(spotListDtos, memberId);
		return new ResultSpotListDto(200l, true, "성공", spotListDtos);
	}

	@Override
	public ResultSpotListDto getSpotListWithoutPriority(Pageable pageable, List locationList, Category category,
		Long memberId) {
		Page<SpotListDto> spotListDtos = spotRepository.searchSpotByLocationAndCategory(memberId,
			locationList, category, pageable);
		postPictureAndBooleanFavoriteOnSpotListDto(spotListDtos, memberId);
		return new ResultSpotListDto(200l, true, "성공", spotListDtos);
	}

	private void postPictureAndBooleanFavoriteOnSpotListDto(Page<SpotListDto> spotListDtos, Long memberId) {
		List<PictureDetailDto> pictureDetailDtos = pictureRepository.getPictureDetailDtoBySpotIdList(spotListDtos.getContent());
		postPictureUrlToDto(spotListDtos.getContent(),pictureDetailDtos);
		List<Long> spotIdList = spotListDtos.getContent().stream().map(o -> o.getSpotId()).collect(Collectors.toList());
		List<Long> favoriteSpotIdList = favoriteSpotRepository.getBooleanFavoriteSpot(memberId, spotIdList);
		postBooleanFavoriteSpotBySpotListDto(spotListDtos.getContent(), favoriteSpotIdList);
	}

	private void postPictureUrlToDto(List<SpotListDto> spotListDtoList, List<PictureDetailDto> pictureDetailDtoList) {
		Map<Long, List<PictureDetailDto>> collect = pictureDetailDtoList.stream()
			.collect(Collectors.groupingBy(p -> p.getSpotId()));

		//사진이 없는 경우
		if (collect.isEmpty()) {
			return;
		}

		spotListDtoList.forEach(sl -> {
			int size = collect.get(sl.getSpotId()).size();
			if (size < 3) {
				sl.setPictureDetailDtoList(collect.get(sl.getSpotId()));
			} else {
				IntStream.range(0, 3)
					.forEach(i -> sl.getPictureDetailDtoList().add(collect.get(sl.getSpotId()).get(i)));
			}
		});
	}


	private void postBooleanFavoriteSpotBySpotListDto(List<SpotListDto> spotListDtoList, List<Long> favoriteSpotIdList) {
		//list 에서 contain 으로 접근하는 것보다 hashset 으로 접근하는게 더 빨라서 set 으로 stream 해줌 (디버깅해보니깐 hashset 으로 들어감)
		//또한 중복된 spotid가 있음 여러개 위시리스트에서 같은 spot 이 있을수 도 있잖음 그래서 set 을 사용하는게 올바른 것임
		Set<Long> favoriteSpotIdSet = favoriteSpotIdList.stream().collect(Collectors.toSet());

		//"존재 하는 것"과 , 따로 spot 리스트 만들고,
		spotListDtoList.stream()
			.filter(i -> favoriteSpotIdSet.contains(i.getSpotId()))
			.forEach(o -> o.setFavorite(true));

		//"존재하지 않는 것"은 spotId와 매칭되지 않는 것도 map 으로 매핑해서 false 값으 입력해줌->ㄴㄴ 기존꺼 활용해서 !로 filter 로 거름
		spotListDtoList.stream()
			.filter(i -> !favoriteSpotIdSet.contains(i.getSpotId()))
			.forEach(o -> o.setFavorite(false));
	}

}




