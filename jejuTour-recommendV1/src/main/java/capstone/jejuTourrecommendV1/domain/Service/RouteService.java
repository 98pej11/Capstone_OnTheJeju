package capstone.jejuTourrecommendV1.domain.Service;

import capstone.jejuTourrecommendV1.domain.Favorite;
import capstone.jejuTourrecommendV1.repository.FavoriteRepository;
import capstone.jejuTourrecommendV1.repository.FavoriteSpotQueryRepository;
import capstone.jejuTourrecommendV1.repository.FavoriteSpotRepository;
import capstone.jejuTourrecommendV1.web.login.exceptionClass.UserException;
import capstone.jejuTourrecommendV1.web.pageDto.favoritePage.SpotListDtoByFavoriteSpot;
import capstone.jejuTourrecommendV1.web.pageDto.routePage.ResultFavoriteSpotList;
import capstone.jejuTourrecommendV1.web.pageDto.routePage.RouteForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RouteService {

    private final EntityManager em;

    private final FavoriteSpotQueryRepository favoriteSpotQueryRepository;
    private final FavoriteSpotRepository favoriteSpotRepository;

    //Todo:
    private final FavoriteRepository favoriteRepository;

    public ResultFavoriteSpotList favoriteSpotList(Long favoriteId){

        Favorite favorite = favoriteRepository.findOptionById(favoriteId).orElseThrow(() -> new UserException("올바르지 않은 favoriteId 입니다"));

        List<SpotListDtoByFavoriteSpot> spotListDtoByFavoriteSpots = favoriteSpotQueryRepository.favoriteSpotList(favoriteId);


        return new ResultFavoriteSpotList(200l,true,"성공",favorite.getName(), spotListDtoByFavoriteSpots);

    }




    public List recommentSpotList(Long favoriteId, RouteForm routeForm){

        //favoriteSpotRepository.findOptionBySpotIdAndFavoriteId()

        List list = favoriteSpotQueryRepository.recommendSpotList(favoriteId, routeForm);

        return list;

    }






}












