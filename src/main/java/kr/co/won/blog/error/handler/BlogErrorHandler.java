package kr.co.won.blog.error.handler;

import kr.co.won.blog.controller.BlogController;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = {BlogController.class})
public class BlogErrorHandler {
}
