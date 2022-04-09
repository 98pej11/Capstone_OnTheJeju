package capstone.jejuTourrecommend.web.api.exhandler.advice;

import capstone.jejuTourrecommend.web.api.exception.UserException;
import capstone.jejuTourrecommend.web.api.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "capstone.jejuTourrecommend.web.api")//피캐지를 지정해서 v2,v3모두 적용가능하게
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExhandler(IllegalArgumentException e){
        log.info("[exceptionHandler] ex",e);
        return new ErrorResult("Bad",e.getMessage());
    }

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




