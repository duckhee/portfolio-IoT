package kr.co.won.blog.error;

import kr.co.won.blog.controller.AdminBlogController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice(basePackageClasses = {AdminBlogController.class})
public class AdminBlogErrorHandler {


}
