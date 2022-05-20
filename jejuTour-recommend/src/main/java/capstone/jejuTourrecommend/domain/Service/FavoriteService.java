package capstone.jejuTourrecommend.domain.Service;


import capstone.jejuTourrecommend.domain.Favorite;
import capstone.jejuTourrecommend.domain.FavoriteSpot;
import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.domain.Spot;
import capstone.jejuTourrecommend.repository.*;
import capstone.jejuTourrecommend.web.favoritePage.FavoriteDto;
import capstone.jejuTourrecommend.web.login.exceptionClass.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.function.ToDoubleBiFunction;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {

    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final SpotRepository spotRepository;
    private final FavoriteSpotRepository favoriteSpotRepository;
    private final FavoriteSpotQueryRepository favoriteSpotQueryRepository;

    private final EntityManager em;

    //사용자의 위시리스트 목록 "폼" 보여주기
    public Page<FavoriteDto> getFavoriteList(String memberEmail, Pageable pageable){


        Member member = memberRepository.findOptionByEmail(memberEmail)
                .orElseThrow(() -> new UserException("가입되지 않은 E-MAIL 입니다."));

        //이거 실험용 데이터임 TODO: 실험용 데이터임
        //PageRequest pageRequest = PageRequest.of(0,100);

        Page<FavoriteDto> favoriteDtoPage = favoriteRepository.findByMember(member,pageable)
                .map(favorite -> new FavoriteDto(favorite.getId(), favorite.getName()));


        //return new GetFavoriteFormDto();
        return favoriteDtoPage;

    }

    // 선택한 관광지를 선태한 위시리스트에 추가
    // 선택한 관광지 정보, 사용자 정보, 위시리스트 정보 필요
    public void postFavoriteForm(String memberEmail, Long spotId, Long favoriteId){

        //솔직히 멤버 정보는 필요 없음 어차피 해당 favorite 은 member 와 연결되어 있음
        //Optional<Member> member = memberRepository.findOptionByEmail(memberEmail);

        //Todo: 여기서 관광지 넣을때 중복 체크해줘야함

        Spot spot = spotRepository.findOptionById(spotId)
                .orElseThrow(() -> new UserException("관광지 id가 맞지 않습니다"));

        Favorite favorite = favoriteRepository.findOptionById(favoriteId)
                .orElseThrow(() -> new UserException("위시리스트 id가 맞지 않습니다"));

        Optional<FavoriteSpot> result = favoriteSpotRepository.findOptionBySpotIdAndFavoriteId(spot.getId(), favorite.getId());
        if(result.isPresent()){
            throw new UserException("위시리스트에 중복된 관광지가 들어 있습니다.");
        }


        FavoriteSpot favoriteSpot = new FavoriteSpot(favorite,spot);
        favoriteSpotRepository.save(favoriteSpot);

    }


    //새로운 위시 리스트를 만들고 해당 관광지 넣기
    // 선택한 관광지 정보, 사용자 정보, 위시리스트 이름 필요
    public void newFavoriteListO(String memberEmail, Long spotId, String favoriteName){

        Member member = memberRepository.findOptionByEmail(memberEmail)
                .orElseThrow(() -> new UserException("가입되지 않은 E-MAIL 입니다."));

        Optional<Spot> spot = spotRepository.findOptionById(spotId);


        // 회원객체와 장소 객체 가져오고
        // favorite하고 favoritespot을 생성하고 넣기

        Favorite favorite = new Favorite(favoriteName,member);

        favoriteRepository.save(favorite);

        FavoriteSpot favoriteSpot = new FavoriteSpot(favorite,spot.get());

        favoriteSpotRepository.save(favoriteSpot);

    }

    //관광지가 없기에 새로운 위시리스트 추가만 함
    public void newFavoriteListX(String memberEmail, String favoriteName){

        Member member = memberRepository.findOptionByEmail(memberEmail)
                .orElseThrow(() -> new UserException("가입되지 않은 E-MAIL 입니다."));

        Favorite favorite = new Favorite(favoriteName,member);

        favoriteRepository.save(favorite);

    }


    //위시 리스트 삭제하기
    //해당 위시리스트 정보 필요
    public void deleteFavoriteList(Long favoriteId){

        //먼저 favoriteSpot들 찾아서 삭제해주고 (여러개임), 이때 favorite,spot필요
        //그다음 favorite을 삭제해줘야함 (한개), favorteId만 필요

        //favoriteSpotRepository.deleteAllById();

        favoriteSpotQueryRepository.deleteFavoriteSpot(favoriteId);

        //favoriteRepository.flush();

        //근데 @Transactional 에서 메서드 끝나고 commit 해줌
        em.flush();
        em.clear();

        favoriteRepository.deleteById(favoriteId);

        em.flush();
        em.clear();

    }


}


























