package capstone.jejuTourrecommend.domain.Service;

import capstone.jejuTourrecommend.domain.Favorite;
import capstone.jejuTourrecommend.repository.FavoriteRepository;
import capstone.jejuTourrecommend.repository.FavoriteSpotQueryRepository;
import capstone.jejuTourrecommend.repository.FavoriteSpotRepository;
import capstone.jejuTourrecommend.web.login.exceptionClass.UserException;
import capstone.jejuTourrecommend.web.pageDto.favoritePage.FavoriteSpotListDto;
import capstone.jejuTourrecommend.web.pageDto.mainPage.SpotListDto;
import capstone.jejuTourrecommend.web.pageDto.routePage.ResultFavoriteSpotList;
import capstone.jejuTourrecommend.web.pageDto.routePage.RouteForm;
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

        List<FavoriteSpotListDto> favoriteSpotListDtos = favoriteSpotQueryRepository.favoriteSpotList(favoriteId);


        return new ResultFavoriteSpotList(200l,true,"성공",favorite.getName(),favoriteSpotListDtos);

    }




    public List recommentSpotList(Long favoriteId, RouteForm routeForm){

        //favoriteSpotRepository.findOptionBySpotIdAndFavoriteId()

        List list = favoriteSpotQueryRepository.recommendSpotList(favoriteId, routeForm);

        return list;

    }






}












