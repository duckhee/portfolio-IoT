package kr.co.won.study.error.handler;

import kr.co.won.study.api.StudyApiController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {StudyApiController.class})
public class StudyApiErrorHandler {
}
