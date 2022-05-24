package capstone.jejuTourrecommend.domain.Service;

import capstone.jejuTourrecommend.repository.FavoriteSpotQueryRepository;
import capstone.jejuTourrecommend.repository.FavoriteSpotRepository;
import capstone.jejuTourrecommend.web.pageDto.mainPage.SpotListDto;
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

    public List<SpotListDto> favoriteSpotList(Long favoriteId){

        List<SpotListDto> spotListDtos = favoriteSpotQueryRepository.favoriteSpotList(favoriteId);

        return spotListDtos;

    }

    public List recommentSpotList(Long favoriteId, RouteForm routeForm){

        //favoriteSpotRepository.findOptionBySpotIdAndFavoriteId()

        List list = favoriteSpotQueryRepository.recommendSpotList(favoriteId, routeForm);

        return list;

    }






}












