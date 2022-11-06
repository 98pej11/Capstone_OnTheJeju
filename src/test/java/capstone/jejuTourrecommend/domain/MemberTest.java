package capstone.jejuTourrecommend.domain;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.authentication.infrastructure.respository.MemberJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@SpringBootTest
public class MemberTest {

	@Autowired
	MemberJpaRepository memberJpaRepository;

	@PersistenceContext
	EntityManager em;

	@Test
	public void memberTest() throws Exception {
		//given
		Member member = new Member("memberA", "123@gmail.com");
		memberJpaRepository.save(member);

		Thread.sleep(100);
		member.setUsername("member2");

		em.flush(); //@PreUpdate
		em.clear();

		//when
		Member findmember = memberJpaRepository.findById(member.getId()).get();

		//then
		System.out.println("findmember.getCreatedDate() = " + findmember.getCreatedDate());
		System.out.println("findmember.getCreatedDate() = " + findmember.getLastModifiedDate());
		System.out.println("findmember.getCreatedBy() = " + findmember.getCreatedBy());
		System.out.println("findmember.getLastModifiedBy() = " + findmember.getLastModifiedBy());

	}

}
















