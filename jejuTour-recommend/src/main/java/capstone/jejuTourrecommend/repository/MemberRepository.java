package capstone.jejuTourrecommend.repository;


import capstone.jejuTourrecommend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {


    Optional<Member> findOptionByEmail(String email); //단건 Optional

    List<Member> findByEmail(String email);

}























































