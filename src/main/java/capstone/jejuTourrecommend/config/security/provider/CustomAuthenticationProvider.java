package capstone.jejuTourrecommend.config.security.provider;

import capstone.jejuTourrecommend.config.security.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;


public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GrantedAuthoritiesMapper grantedAuthoritiesMapper;

    /**
     * 검증위한 메서드
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(username);


        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("BadCredentialsException");//비밀번호가 틀린거

        }


        //인증에 성공한 최종적인 인증객체를 반환하는 것임 -> 이게 인증 객체 역할임!!!
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), null, userDetails.getAuthorities());

        return authenticationToken;


//        EmailPwAuthenticationToken emailPwAuthenticationToken = new EmailPwAuthenticationToken(
//                userDetails.getUsername(), userDetails.getPassword(),
//                grantedAuthoritiesMapper.mapAuthorities(userDetails.getAuthorities()));
//
//        emailPwAuthenticationToken.setDetails(authentication.getDetails());
//
//        return emailPwAuthenticationToken;

    }

    /**
     * aClass 타입과 CustomAuthenticationProvider가 제고하는 타입이 일치하는지 보는 것임
     *
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }
}