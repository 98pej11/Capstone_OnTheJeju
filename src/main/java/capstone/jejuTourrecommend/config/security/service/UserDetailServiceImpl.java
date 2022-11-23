package capstone.jejuTourrecommend.config.security.service;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.authentication.infrastructure.respository.MemberJpaRepository;
import capstone.jejuTourrecommend.config.security.dto.AccountContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@RequiredArgsConstructor
@Service("userDetailsService")//Todo: 수정된거 이렇게 하면 private final UserDetailService userserivce라고 해도 알아서 인식함
public class UserDetailServiceImpl implements UserDetailsService {

	private final MemberJpaRepository memberJpaRepository;

	/**
	 * loadUserByUsername 메서드에서 실제 db의 회원정보를 가져온다
	 *
	 * @param email
	 * @return value: 캐시 이름 , key : 해당 캐시 이름의 key //
	 */
	//@Cacheable(value = "member", key = "#email", unless = "#result == null")
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Member member = memberJpaRepository.findOptionByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));

		List<GrantedAuthority> grantedAuthorities = Collections
			.singletonList(new SimpleGrantedAuthority(member.getRole()));

		return new AccountContext(member, grantedAuthorities);
	}

	private org.springframework.security.core.userdetails.User createSpringSecurityUser(Member member) {

		List<GrantedAuthority> grantedAuthorities = Collections
			.singletonList(new SimpleGrantedAuthority(member.getRole()));

		return new User(member.getEmail(), member.getPassword(), grantedAuthorities);
	}

}















