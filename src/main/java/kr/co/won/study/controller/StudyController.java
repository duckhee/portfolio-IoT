package kr.co.won.study.controller;

import kr.co.won.auth.AuthUser;
import kr.co.won.study.form.CreateStudyForm;
import kr.co.won.study.service.StudyService;
import kr.co.won.study.validation.CreateStudyValidation;
import kr.co.won.user.domain.UserDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @InitBinder(value = {"createStudyForm"})
    public void CreateStudyValidator(WebDataBinder binder) {
        binder.addValidators(createStudyValidation);
    }

    @GetMapping(path = "/create")
    public String createStudyPage(@AuthUser UserDomain loginUser, Model model) {
        if (loginUser == null) {
            return "redirect:/login";
        }
        model.addAttribute(new CreateStudyForm());
        model.addAttribute("user", loginUser);
        return "study/createPage";
    }

    @PostMapping(path = "/create")
    public String createStudyDo(@AuthUser UserDomain loginUser, Model model, @Validated CreateStudyForm form, Errors errors, RedirectAttributes flash){
        if(errors.hasErrors()){
            model.addAttribute("user", loginUser);
            return "";
        }
        return "redirect:/study/";
    }
}
