package kr.co.won.study.error.handler;

import kr.co.won.study.controller.AdminStudyController;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = {AdminStudyController.class})
public class StudyAdminErrorHandler {
}
