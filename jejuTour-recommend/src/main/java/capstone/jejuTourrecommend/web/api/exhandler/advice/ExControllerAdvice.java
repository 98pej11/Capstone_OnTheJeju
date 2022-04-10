package capstone.jejuTourrecommend.web.api.exhandler.advice;

import capstone.jejuTourrecommend.web.api.exception.UserException;
import capstone.jejuTourrecommend.web.api.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j//*****************여기 패치지 지정한거 보셈*****이렇게 페키지 별로 오류설정 가능함(로그인이면 로그인 api면 api)
@RestControllerAdvice(basePackages = "capstone.jejuTourrecommend.web.api")//피캐지를 지정해서 v2,v3모두 적용가능하게
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)//IllegalArgumentException 오류 오면 반응함
    public ErrorResult illegalExhandler(IllegalArgumentException e){
        log.info("[exceptionHandler] ex",e);

        //IllegalArgumentException 오류 발생했을때 e에 메세지도 같이 들어 있음
        // 그것도 ErrorResult 에 넣어서 객체 반환해줌
        return new ErrorResult("Bad",e.getMessage());
    }


    //ResponseEntity에 에러를 담아서 보냄
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResult> userExHandler(UserException e){
        log.error("[exceptionHandler] ex",e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e){
        log.error("[exceptionHandler] ex",e);
        return new ErrorResult("EX","내부 오류");
    }
}




