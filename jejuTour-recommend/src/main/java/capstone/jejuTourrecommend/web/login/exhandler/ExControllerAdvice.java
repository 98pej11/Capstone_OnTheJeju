package capstone.jejuTourrecommend.web.login.exhandler;

import capstone.jejuTourrecommend.web.login.exceptionClass.JwtException;
import capstone.jejuTourrecommend.web.login.exceptionClass.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j//*****************여기 패치지 지정한거 보셈*****이렇게 페키지 별로 오류설정 가능함(로그인이면 로그인 api면 api)
@RestControllerAdvice(basePackages = "capstone.jejuTourrecommend.web.login")//피캐지를 지정해서 v2,v3모두 적용가능하게
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)//IllegalArgumentException 오류 오면 반응함
    public ErrorResult illegalExhandler(IllegalArgumentException e){
        log.info("[exceptionHandler] ex",e);

        //IllegalArgumentException 오류 발생했을때 e에 메세지도 같이 들어 있음
        // 그것도 ErrorResult 에 넣어서 객체 반환해줌
        return new ErrorResult(400,false,e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResult illegalStateExhandler(IllegalStateException e){
        log.info("[exceptionHandler] ex",e);

        return  new ErrorResult(400,false,e.getMessage());
    }

    //ResponseEntity에 에러를 담아서 보냄
    @ExceptionHandler(UserException.class)
    public ErrorResult userExHandler(UserException e){
        log.error("[exceptionHandler] ex",e);
        ErrorResult errorResult = new ErrorResult(400,false, e.getMessage());
        return new ErrorResult(400,false,e.getMessage());
    }


    @ExceptionHandler(JwtException.class)
    public ErrorResult JwtExceptionExhandler(JwtException e){
        log.info("[exceptionHandler] ex",e);

        return new ErrorResult(400,false,e.getMessage());
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e){
        log.error("[exceptionHandler] ex",e);
        return new ErrorResult(400,false,"내부 오류");
    }
}




