package capstone.jejuTourrecommend.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.authentication.infrastructure.respository.MemberJpaRepository;

@SpringBootTest
@Transactional
	//@Rollback(value = false)
class MemberJpaRepositoryTest {

	@Autowired
	MemberJpaRepository memberJpaRepository;

	@PersistenceContext
	EntityManager em;

	@Test
	public void testMember() throws Exception {

		System.out.println("memberRepository.getClass() = " + memberJpaRepository.getClass());

		Member member = new Member("123@naver.com");

		memberJpaRepository.save(member);

		//optional 사용예제
		Member result1 = memberJpaRepository.findOptionByEmail(
			member.getEmail()).orElseGet(() -> new Member("no name"));

		assertThat(result1).isEqualTo(member);

		List<Member> all = memberJpaRepository.findAll();
		assertThat(all).containsExactly(member);

		Member findMember = memberJpaRepository.findById(member.getId()).get();
		assertThat(findMember).isEqualTo(member);

	}

}
