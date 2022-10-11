package capstone.jejuTourrecommend.wishList.domain.service;


import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot.SpotJpaRepository;
import capstone.jejuTourrecommend.wishList.domain.Favorite;
import capstone.jejuTourrecommend.wishList.domain.FavoriteSpot;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteDto;
import capstone.jejuTourrecommend.wishList.domain.dto.SpotListDtoByFavoriteSpot;
import capstone.jejuTourrecommend.wishList.domain.repository.FavoriteRepository;
import capstone.jejuTourrecommend.wishList.domain.repository.FavoriteSpotRepository;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.FavoriteForm;
import capstone.jejuTourrecommend.wishList.presentation.dto.response.ResultFavoriteSpotList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteCommandService implements FavoriteCommandUseCase{

    private final FavoriteRepository favoriteRepository;
    private final FavoriteSpotRepository favoriteSpotRepository;
    private final SpotJpaRepository spotJpaRepository;

    /**
     * 선택한 관광지를 선택한 위시리스트에 추가
     * 선택한 관광지 정보, 사용자 정보, 위시리스트 정보 필요
     *
     * @param favoriteForm
     */
    public void postFavoriteForm(FavoriteForm favoriteForm) {

        Spot spot = spotJpaRepository.findOptionById(favoriteForm.getSpotId())
                .orElseThrow(() -> new UserException("관광지 id가 맞지 않습니다"));

        Favorite favorite = favoriteRepository.findOptionById(favoriteForm.getFavoriteId())
                .orElseThrow(() -> new UserException("위시리스트 id가 맞지 않습니다"));

        //관광지 id , 위시리스트 id는 고유의 번호라 member 까지 확인할 필요 없음
        Optional<FavoriteSpot> optionBySpotIdAndFavoriteId = favoriteSpotRepository.findOptionBySpotIdAndFavoriteId(spot.getId(), favorite.getId());

        if (optionBySpotIdAndFavoriteId.isPresent()) {
            throw new UserException("위시리스트에 중복된 관광지가 들어 있습니다.");
        }

        FavoriteSpot favoriteSpot = new FavoriteSpot(favorite, spot);
        favoriteSpotRepository.save(favoriteSpot);

    }


    /**
     * 관광지 정보0
     * 새로운 위시 리스트를 만들고 해당 관광지 넣기
     * 선택한 관광지 정보, 사용자 정보, 위시리스트 이름 필요
     *
     * 관광지 정보X
     * 새로운 위시 리스트를 만들고 해당 관광지 넣기
     * 선택한 관광지 정보, 사용자 정보, 위시리스트 이름 필요
     * (추가 사항)관광지가 없기에 새로운 위시리스트 추가만 함
     *
     * @param member
     * @param spotId
     * @param favoriteName
     * @return
     */
    public FavoriteDto newFavoriteList(Member member, Long spotId, String favoriteName) {

        Optional<Favorite> optionByName = favoriteRepository.findOptionByNameAndMemberId(favoriteName,member.getId());
        if (optionByName.isPresent()) {
            throw new UserException("동일한 위시리스트 이름이 존재합니다");
        }

        Favorite favorite = new Favorite(favoriteName, member);
        FavoriteDto favoriteDto;
        favoriteRepository.save(favorite);


        if (spotId != null) {//관광지 정보가 있으면, 위에서 만든 위시리스트 객체에 데이터 넣어주고 favoriteSpot 도 만들어주기

            Optional<Spot> spot = spotJpaRepository.findOptionById(spotId);

            FavoriteSpot favoriteSpot = new FavoriteSpot(favorite, spot.get());
            favoriteSpotRepository.save(favoriteSpot);

        }
        //데이터 잘 들어갔는지 확인 및 오류시 메세지 설정
        favoriteDto = favoriteRepository.findOptionByNameAndMemberId(favoriteName,member.getId())
                .map(favorite1 -> new FavoriteDto(favorite1.getId(), favorite1.getName()))
                .orElseThrow(() -> new UserException("갱신 성공을 못하였습니다"));

        return favoriteDto;
    }


    /**
     * 위시 리스트 삭제하기
     * 해당 위시리스트 정보 필요
     *
     * @param favoriteId
     */
    public void deleteFavoriteList(Long favoriteId) {

        favoriteRepository.findOptionById(favoriteId)
                .orElseThrow(() -> new UserException("올바르지 않는 favoriteId 입니다"));

        favoriteSpotRepository.deleteFavoriteSpotByFavoriteId(favoriteId);

        favoriteRepository.deleteById(favoriteId);


    }

    public void deleteSpotInFavoriteList(Long favoriteId, Long spotId) {

        favoriteRepository.findOptionById(favoriteId)
                .orElseThrow(() -> new UserException("올바르지 않는 favoriteId 입니다"));

        spotJpaRepository.findOptionById(spotId)
                .orElseThrow(() -> new UserException("올바르지 않는 spotId 입니다"));

        favoriteSpotRepository.deleteByFavoriteIdAndSpotId(favoriteId, spotId);
    }





}
