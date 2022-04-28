package capstone.jejuTourrecommend.domain.Service;


import capstone.jejuTourrecommend.domain.Favorite;
import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.repository.FavoriteRepository;
import capstone.jejuTourrecommend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteListService {
    
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;


    //위시 리스트 페이지
    //사용자 정보 필요
    @Transactional
    public void findFavoriteList(Long memberId){


        Optional<Member> member = memberRepository.findById(memberId);

        Page<Favorite> byMember = favoriteRepository.findByMember(member.get());


    }
    
    
    
    
    
    
    
    
}












