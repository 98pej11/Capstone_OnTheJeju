package capstone.jejuTourrecommend.domain.Service;

import capstone.jejuTourrecommend.repository.FavoriteSpotQueryRepository;
import capstone.jejuTourrecommend.web.mainPage.ResultSpotListDto;
import capstone.jejuTourrecommend.web.mainPage.SpotListDto;
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

    public List<SpotListDto> favoriteSpotList(Long favoriteId){

        List<SpotListDto> spotListDtos = favoriteSpotQueryRepository.favoriteSpotList(favoriteId);

        return spotListDtos;

    }

    public List recommentSpotList(Long favoriteId){

        List list = favoriteSpotQueryRepository.recommendSpotList(favoriteId);

        return list;

    }






}












