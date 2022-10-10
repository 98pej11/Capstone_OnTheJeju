package capstone.jejuTourrecommend.config;

import capstone.jejuTourrecommend.config.security.handler.CustomAuthenticationFailureHandler;
import capstone.jejuTourrecommend.config.security.handler.RestAuthenticationEntryPoint;
import capstone.jejuTourrecommend.config.security.provider.JwtTokenProvider;
import capstone.jejuTourrecommend.config.security.filter.CustomLoginProcessingAuthenticationFilter;
import capstone.jejuTourrecommend.config.security.filter.JwtAuthenticationFilter;
import capstone.jejuTourrecommend.config.security.handler.CustomAuthenticationSuccessHandler;
import capstone.jejuTourrecommend.config.security.handler.RestAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


//Spring Security를 사용하기 위해서는 Spring Security Filter Chain 을 사용한다는 것을 명시해 주기 위해 @EnableWebSecurity 사용
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    // 암호화에 필요한 PasswordEncoder 를 Bean 등록합니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // authenticationManager를 Bean 등록합니다.
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 정적 자원들 필터 처리 안하도록 설정한 것임
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    private static final RequestMatcher LOGIN_REQUEST_MATCHER = new AntPathRequestMatcher("/login","POST");
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;


    @Override
    protected void configure(HttpSecurity http) throws Exception {



        http.headers().frameOptions().disable();

        http.authorizeRequests()
                .antMatchers("/h2-console/**")
                .permitAll(); // 누구나 h2-console 접속 허용

        http
                .httpBasic().disable() // rest api 만을 고려하여 기본 설정은 해제하겠습니다.
                //.formLogin().disable()
                .csrf().disable() // csrf 보안 토큰 disable처리.
                .logout().disable() // jwt 토큰을 이용할거라서 logout 은 disable 설정해 놓음
                //그리고 security는 기본으로 세션으로 회원정보를 저장하는데, jwt 를 사용할거라 무상태(stateless)로 해 놓음
                .sessionManagement().sessionCreationPolicy(STATELESS)


                .and()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // 인증 실패
                .accessDeniedHandler(new RestAccessDeniedHandler()) // 인가 실패




                // 토큰 기반 인증이므로 세션 역시 사용하지 않습니다.
                .and()
                .authorizeRequests() // 요청에 대한 사용권한 체크
                // "/admin/**", "/user/**" 형식의 URL 로 들어오는 요청에 대해 인증을 요구하는 부분.
                // 인증을 요구하는 경로로 요청이 들어오면 인증 요청을 한다
                .antMatchers("/admin/**").access("hasRole('ADMIN') or hasRole('USER')")
                //이제 회원 가입된 사람만 접근하게하려는 uri는 앞에 이 admin붙여주면됨, user면 user로
                .antMatchers("/user/**").hasRole("USER")
                .anyRequest().permitAll() // 그외 나머지 요청은 누구나면접근 가능

                .and()
                .addFilterAt(customLoginProcessingAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, CustomLoginProcessingAuthenticationFilter.class);


        http.cors();



    }

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public CustomLoginProcessingAuthenticationFilter customLoginProcessingAuthenticationFilter() throws Exception {

        CustomLoginProcessingAuthenticationFilter customLoginProcessingAuthenticationFilter
                = new CustomLoginProcessingAuthenticationFilter(LOGIN_REQUEST_MATCHER);

        customLoginProcessingAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());

        customLoginProcessingAuthenticationFilter
                .setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);

        customLoginProcessingAuthenticationFilter
                .setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        return customLoginProcessingAuthenticationFilter;

    }


}
