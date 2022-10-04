//package capstone.jejuTourrecommend.web;
//
//import capstone.jejuTourrecommend.web.login.dto.AccountContext;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.MethodParameter;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.support.WebDataBinderFactory;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.ModelAndViewContainer;
//
//@Slf4j
//public class CustomArgumentResolver implements HandlerMethodArgumentResolver {
//
//    @Override
//    public boolean supportsParameter(MethodParameter methodParameter) {
//        Class<?> parameterType = methodParameter.getParameterType();
//        return AccountContext.class.equals(parameterType);
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter methodParameter,
//                                  ModelAndViewContainer modelAndViewContainer,
//                                  NativeWebRequest nativeWebRequest,
//                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
//        Object principal = null;
//        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)  SecurityContextHolder.getContext().getAuthentication();
//
//
//
//        if(authentication != null ) {
//            log.info("authentication.getPrincipal() ={}",authentication.getPrincipal());
//            principal = authentication.getPrincipal();
//        }
//        if(principal == null || principal.getClass() == String.class) {
////            return null;
//        }
//
//        return principal;
//    }
//
//}
