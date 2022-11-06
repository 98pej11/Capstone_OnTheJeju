package capstone.jejuTourrecommend.authentication.infrastructure.respository;

import capstone.jejuTourrecommend.authentication.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

	Optional<Member> findOptionByEmail(String email); //단건 Optional

	//Optional<Member> findById(Long id);

	List<Member> findByEmail(String email);

	Optional<Member> findOptionByRefreshToken(String refreshToken);

}























































