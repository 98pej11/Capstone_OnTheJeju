package capstone.jejuTourrecommend.authentication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

/**
 * 만약, domain 으로 생성된 객체 클래스명이 Member 아니라 예를 들어 LoginUser 라면 SpEL에 member 가 아니라 loginUser 를 반환하도록 해야 합니다.
 */
@Retention(RetentionPolicy.RUNTIME) // runtime시 까지 유지
@Target(ElementType.PARAMETER) // 파라미터에 사용할 것
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : member")
public @interface LoginUser {
}
