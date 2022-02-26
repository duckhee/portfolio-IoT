package kr.co.won.blog.error.handler;

import kr.co.won.blog.api.BlogApiController;
import kr.co.won.blog.api.BlogReplyApiController;
import kr.co.won.blog.api.BlogResourceApiController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {
        BlogApiController.class,
        BlogReplyApiController.class,
        BlogResourceApiController.class
})
public class BlogApiErrorHandler {
}
