package capstone.JejuTourRecommend.web.argumentresolver;


import capstone.JejuTourRecommend.domain.Member;
import capstone.JejuTourRecommend.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    //@Login 애노테이션이 있으면서 Member 타입이면 해당 ArgumentResolver 가 사용된다.
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasMemberType;
    }

    //컨트롤러 호출 직전에 호출 되어서 필요한 파라미터 정보를 생성해준다
    //여기서는 세션에 있는 로그인 회원 정보인 member 객체를 찾아서 반환해준다
    //이후 스프링 MVC는 컨트롤러의 메서드를 호출하면서 여기에서 반환된 member 객체를 파라미터에 전달해준다.
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        log.info("resolveArgument 실행");

        //HttpServletRequest을 아래와 같이 getNativeRequest로 뽑아야함
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        //만약 세션을 조회했는데 없으면 null 반환하려고 옵션은 false로 둠
        HttpSession session = request.getSession(false);
        if(session==null){
            return null;
        }
        //참고사항: 세션에 <키, value> 형식인데 키가 LOGIN_MEMBER고, value가 로그인한 member객체임
        Object member = session.getAttribute(SessionConst.LOGIN_MEMBER);

        return member;
    }

}
