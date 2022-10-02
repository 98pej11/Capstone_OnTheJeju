package capstone.jejuTourrecommendV1.repository;


import capstone.jejuTourrecommendV1.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findOptionByEmail(String email); //단건 Optional

    Optional<Member> findById(Long id);

    List<Member> findByEmail(String email);

    Optional<Member> findOptionByRefreshToken(String refreshToken);

}























































