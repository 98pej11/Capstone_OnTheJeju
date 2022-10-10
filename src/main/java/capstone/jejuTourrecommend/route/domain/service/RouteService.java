package capstone.jejuTourrecommend.route.domain.service;

import capstone.jejuTourrecommend.favorite.domain.Favorite;
import capstone.jejuTourrecommend.favorite.infrastructure.repository.FavoriteJpaRepository;
import capstone.jejuTourrecommend.favorite.infrastructure.repository.FavoriteSpotQuerydslRepository;
import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.favorite.domain.dto.SpotListDtoByFavoriteSpot;
import capstone.jejuTourrecommend.route.presentation.dto.response.ResultFavoriteSpotList;
import capstone.jejuTourrecommend.route.presentation.dto.request.RouteForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RouteService {

    private final FavoriteSpotQuerydslRepository favoriteSpotQuerydslRepository;

    private final FavoriteJpaRepository favoriteJpaRepository;

    public ResultFavoriteSpotList favoriteSpotList(Long favoriteId){

        Favorite favorite = favoriteJpaRepository.findOptionById(favoriteId).orElseThrow(() -> new UserException("올바르지 않은 favoriteId 입니다"));

        List<SpotListDtoByFavoriteSpot> spotListDtoByFavoriteSpots = favoriteSpotQuerydslRepository.favoriteSpotList(favoriteId);

        return new ResultFavoriteSpotList(200l,true,"성공",favorite.getName(), spotListDtoByFavoriteSpots);

    }


    public List recommendSpotList(Long favoriteId, RouteForm routeForm){


        List list = favoriteSpotQuerydslRepository.recommendSpotList(favoriteId, routeForm);

        return list;

    }






}












