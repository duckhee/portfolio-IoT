package kr.co.won.study.controller;

import kr.co.won.study.service.StudyService;
import kr.co.won.study.validation.CreateStudyValidation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/study")
public class StudyController {

    private final ModelMapper modelMapper;

    /**
     * validation create study
     */
    private final CreateStudyValidation createStudyValidation;

    private final StudyService studyService;


}
