package capstone.jejuTourrecommend.wishList.domain.service;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot.SpotJpaRepository;
import capstone.jejuTourrecommend.wishList.domain.Favorite;
import capstone.jejuTourrecommend.wishList.domain.FavoriteSpot;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteDto;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import capstone.jejuTourrecommend.wishList.domain.dto.SpotListDtoByFavoriteSpot;
import capstone.jejuTourrecommend.wishList.domain.repository.FavoriteRepository;
import capstone.jejuTourrecommend.wishList.domain.repository.FavoriteSpotRepository;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.FavoriteForm;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.RouteForm;
import capstone.jejuTourrecommend.wishList.presentation.dto.response.ResultFavoriteSpotList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteQueryService implements FavoriteQueryUseCase{


    private final FavoriteRepository favoriteRepository;
    private final FavoriteSpotRepository favoriteSpotRepository;

    /**
     * 사용자의 위시리스트 목록 "폼"+ 위시리스트 페이지 보여주기 + "여러 사진"도 줌
     * 폼 api 사용할때는 사진 여러장 중 하나만 고르면 되니깐 ㄱㅊ
     * readonly
     * @param memberId
     * @param pageable
     * @return
     */
    public Page<FavoriteListDto> getFavoriteList(Long memberId, Pageable pageable) {


        //이거 실험용 데이터임 TODO: 실험용 데이터임
        //PageRequest pageRequest = PageRequest.of(0,100);

        Page<FavoriteListDto> favoriteListDtos = favoriteSpotRepository.getFavoriteList(memberId, pageable);


        return favoriteListDtos;


    }

    public List recommendSpotList(Long favoriteId, RouteForm routeForm){


        List list = favoriteSpotRepository.recommendSpotList(favoriteId, routeForm);

        return list;

    }

    public ResultFavoriteSpotList favoriteSpotList(Long favoriteId){

        Favorite favorite = favoriteRepository.findOptionById(favoriteId).orElseThrow(() -> new UserException("올바르지 않은 favoriteId 입니다"));

        List<SpotListDtoByFavoriteSpot> spotListDtoByFavoriteSpots = favoriteSpotRepository.favoriteSpotList(favoriteId);

        return new ResultFavoriteSpotList(200l,true,"성공",favorite.getName(), spotListDtoByFavoriteSpots);

    }






}
