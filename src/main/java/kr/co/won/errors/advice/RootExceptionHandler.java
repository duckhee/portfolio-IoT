package kr.co.won.errors.advice;

import kr.co.won.blog.api.BlogApiController;
import kr.co.won.errors.resource.AccessDeniedErrorResource;
import kr.co.won.errors.resource.dto.ErrorDto;
import kr.co.won.user.api.UserApiController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice(basePackageClasses = {
        UserApiController.class,
        BlogApiController.class
})
@RequiredArgsConstructor
public class RootExceptionHandler {

    @ExceptionHandler
    public ResponseEntity rootExceptionsResources(Exception exception) {
        log.info("exceptions ::: {}", exception.toString());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity accessDeniedExceptionResources(Exception exception, HttpServletRequest request) {
        log.info("access denied");
        ErrorDto errorDto = ErrorDto.builder()
                .message(exception.getMessage())
                .uri(request.getRequestURI())
                .build();
        EntityModel<ErrorDto> errorResources = AccessDeniedErrorResource.modelOf(errorDto);
        log.info("error resources ::: {}", errorResources.toString());
        return ResponseEntity.badRequest().body(errorResources);
    }
}
