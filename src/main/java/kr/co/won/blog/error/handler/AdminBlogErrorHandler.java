package kr.co.won.blog.error.handler;

import kr.co.won.blog.controller.AdminBlogController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice(
        annotations = {Controller.class},
        assignableTypes = {
        AdminBlogController.class})
public class AdminBlogErrorHandler {


}
