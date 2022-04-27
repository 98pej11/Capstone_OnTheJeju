package capstone.jejuTourrecommend.domain.Service;


import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.domain.MemberSpot;
import capstone.jejuTourrecommend.domain.Spot;
import capstone.jejuTourrecommend.repository.*;
import capstone.jejuTourrecommend.web.spotPage.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotService {

    private final SpotRepository spotRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final PictureRepository pictureRepository;
    private final MemberSpotRepository memberSpotRepository;


    @Transactional
    public SpotDetailDto spotPage(Long spotId, Long memberId){

        Optional<Spot> spot = spotRepository.findOptionById(spotId);
        Optional<SpotDto> spotDto = spot.map(spot1 -> new SpotDto(spot1));

        Optional<Member> member = memberRepository.findById(memberId);
        log.info("member = {}",member);

        //이거 실험용 데이터임
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<ReviewDto> reviewDtoList = reviewRepository.searchSpotReview(spot.get(), pageRequest);

//        List<ReviewDto> reviewDtoList = reviewRepository.findBySpot(
//                spot.get()).stream().map(review -> new ReviewDto(review))
//                .collect(Collectors.toList());

        List<PictureDto> pictureDtoList = pictureRepository.findBySpot(
                        spot.get()).stream().map(picture -> new PictureDto(picture))
                .collect(Collectors.toList());

        Double userScore = memberSpotRepository.findBySpotAndMember(spot.get(), member.get())
                .orElseGet(()->new MemberSpot(0d)).getScore();

        ScoreDto scoreDto = spotRepository.searchScore(spot.get());


        return new SpotDetailDto(spotDto.get(),scoreDto,pictureDtoList,reviewDtoList, userScore.doubleValue());

    }



}





