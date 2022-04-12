package kr.co.won.errors.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class RootControllerExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity rootBadResponse(Exception exception, HttpServletRequest request) {
        log.info("get exception url ::: {}, exception ::: {}", request.getRequestURI(), exception);
        return ResponseEntity.badRequest().build();
    }
}
