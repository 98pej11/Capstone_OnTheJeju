package capstone.jejuTourrecommend.domain.Service;


import capstone.jejuTourrecommend.domain.Favorite;
import capstone.jejuTourrecommend.domain.FavoriteSpot;
import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.domain.Spot;
import capstone.jejuTourrecommend.repository.FavoriteRepository;
import capstone.jejuTourrecommend.repository.FavoriteSpotRepository;
import capstone.jejuTourrecommend.repository.MemberRepository;
import capstone.jejuTourrecommend.repository.SpotRepository;
import capstone.jejuTourrecommend.web.favoritePage.FavoriteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final SpotRepository spotRepository;
    private final FavoriteSpotRepository favoriteSpotRepository;

    @Transactional
    public Page<FavoriteDto> getFavoriteList(Long memberId){

        Optional<Member> member = memberRepository.findById(memberId);


        Page<FavoriteDto> favoriteDtoPage = favoriteRepository.findByMember(member.get())
                .map(favorite -> new FavoriteDto(favorite.getId(), favorite.getName()));


        //return new GetFavoriteFormDto();
        return favoriteDtoPage;

    }

    // 선택한 관광지를 선태한 위시리스트에 추가
    // 선택한 관광지 정보, 사용자 정보, 위시리스트 정보 필요
    @Transactional
    public void postFavoriteForm(Long memberId, Long spotId, Long favoriteId){

        //솔직히 멤버 정보는 필요 없음 어차피 해당 favorite 은 member 와 연결되어 있음
        Optional<Member> member = memberRepository.findById(memberId);

        Optional<Spot> spot = spotRepository.findOptionById(spotId);

        Optional<Favorite> favorite = favoriteRepository.findOptionById(favoriteId);

        FavoriteSpot favoriteSpot = new FavoriteSpot(favorite.get(),spot.get());


    }


    //새로운 위시 리스트를 만들고 해당 관광지 넣기
    // 선택한 관광지 정보, 사용자 정보, 위시리스트 이름 필요
    @Transactional
    public void newFavoriteListO(Long memberId, Long spotId, String favoriteName){

        Optional<Member> member = memberRepository.findById(memberId);

        Optional<Spot> spot = spotRepository.findOptionById(spotId);


        // 회원객체와 장소 객체 가져오고
        // favorite하고 favoritespot을 생성하고 넣기

        Favorite favorite = new Favorite(favoriteName,member.get());

        FavoriteSpot favoriteSpot = new FavoriteSpot(favorite,spot.get());

    }

    //있으면 위시리스트 생성 및 관광지 추가, 없으면 그냥 위시리스트 생성

    @Transactional
    public void newFavoriteListX(Long memberId, String favoriteName){

        Optional<Member> member = memberRepository.findById(memberId);

        Favorite favorite = new Favorite(favoriteName,member.get());

    }


    //위시 리스트 삭제하기
    //해당 위시리스트 정보 필요
    @Transactional
    public void deleteFavoriteList(){

        //먼저 favoriteSpot들 찾아서 삭제해주고 (여러개임), 이때 favorite,spot필요
        //그다음 favorite을 삭제해줘야함 (한개), favorteId만 필요

        favoriteSpotRepository.deleteAllById();
        


    }


}












