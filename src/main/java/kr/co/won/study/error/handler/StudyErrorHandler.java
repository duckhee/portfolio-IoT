package kr.co.won.study.error.handler;

import kr.co.won.study.controller.StudyController;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = {StudyController.class})
public class StudyErrorHandler {
}
