package kr.co.won.blog.error.handler;

import kr.co.won.blog.api.BlogApiController;
import kr.co.won.blog.api.BlogReplyApiController;
import kr.co.won.blog.api.BlogResourceApiController;
import kr.co.won.blog.error.BlogError;
import kr.co.won.main.MainApiController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice(assignableTypes = {
        BlogApiController.class,
        BlogReplyApiController.class,
        BlogResourceApiController.class
})
public class BlogApiErrorHandler {


    /**
     * API IllegalArgumentException Handler
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity wrongArgumentExceptionResource(HttpServletRequest request, Exception exception) {
        String requestURI = request.getRequestURI();
        log.warn("request ::: {}, wrong input value ::: {}", requestURI, exception);
        BlogError blogErrorDto = new BlogError(requestURI, exception);
        RepresentationModel exceptionResource = RepresentationModel.of(blogErrorDto);
        WebMvcLinkBuilder webMvcLinkBuilder = WebMvcLinkBuilder.linkTo(MainApiController.class);
        exceptionResource.add(webMvcLinkBuilder.withRel("index"));
        return ResponseEntity.badRequest().body(exceptionResource);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity accessDeniedExceptionResource(HttpServletRequest request, Exception exception) {
        String requestURI = request.getRequestURI();
        log.warn("request ::: {}, access denied ::: {}", requestURI, exception);
        BlogError blogErrorDto = new BlogError(requestURI, exception);
        RepresentationModel exceptionResource = RepresentationModel.of(blogErrorDto);
        WebMvcLinkBuilder webMvcLinkBuilder = WebMvcLinkBuilder.linkTo(MainApiController.class);
        exceptionResource.add(webMvcLinkBuilder.withRel("index"));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionResource);
    }
}

