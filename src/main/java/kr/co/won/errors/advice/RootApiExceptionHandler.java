package kr.co.won.errors.advice;

import kr.co.won.blog.api.BlogApiController;
import kr.co.won.errors.resource.AccessDeniedErrorResource;
import kr.co.won.errors.resource.dto.ErrorDto;
import kr.co.won.study.api.StudyApiController;
import kr.co.won.user.api.UserApiController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice(
        annotations = {RestController.class})
@RequiredArgsConstructor
public class RootApiExceptionHandler {
    /**
     * root exception handle
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity rootExceptionsResources(Exception exception) {
        log.info("exceptions ::: {}", exception.toString());
        return ResponseEntity.badRequest().build();
    }


    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity accessDeniedExceptionResources(Exception exception, HttpServletRequest request) {
        log.info("access denied");
        ErrorDto errorDto = ErrorDto.builder()
                .message(exception.getLocalizedMessage())
                .uri(request.getRequestURI())
                .build();
        EntityModel<ErrorDto> errorResources = AccessDeniedErrorResource.modelOf(errorDto);
        exception.printStackTrace();
        log.error("access denied uri ::: {}, url ::: {}", request.getRequestURI(), request.getRequestURL());
        return ResponseEntity.badRequest().body(errorResources);
    }

    /**
     * exception Argument Handler
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity argumentErrorResource(Exception exception, HttpServletRequest request) {
        log.info("wrong call ");
        ErrorDto errorDto = ErrorDto.builder()
                .message(exception.getMessage())
                .uri(request.getRequestURI())
                .build();
        EntityModel<ErrorDto> errorResources = AccessDeniedErrorResource.modelOf(errorDto);
        exception.printStackTrace();
        log.error("wrong parameter denied uri ::: {}, url ::: {}", request.getRequestURI(), request.getRequestURL());
        return ResponseEntity.badRequest().body(errorResources);
    }


}
