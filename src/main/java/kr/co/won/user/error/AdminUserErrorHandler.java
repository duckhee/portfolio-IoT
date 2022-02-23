package kr.co.won.user.error;

import kr.co.won.user.controller.AdminUserController;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = {AdminUserController.class})
public class AdminUserErrorHandler {
}
