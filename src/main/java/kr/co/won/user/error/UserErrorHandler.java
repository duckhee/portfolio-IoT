package kr.co.won.user.error;

import kr.co.won.user.controller.UserController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = {UserController.class})
public class UserErrorHandler {
}
