package kr.co.won.study.controller;

import kr.co.won.auth.AuthUser;
import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.form.CreateStudyForm;
import kr.co.won.study.form.UpdateStudyForm;
import kr.co.won.study.service.StudyService;
import kr.co.won.study.validation.CreateStudyValidation;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.type.UserRoleType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/study")
public class StudyController {

    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    /**
     * validation create study
     */
    private final CreateStudyValidation createStudyValidation;

    /**
     * study Service
     */
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
    public String createStudyDo(@AuthUser UserDomain loginUser, Model model, @Validated CreateStudyForm form, Errors errors, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            model.addAttribute("user", loginUser);
            return "";
        }
        StudyDomain newStudy = modelMapper.map(form, StudyDomain.class);
        StudyDomain savedStudy = studyService.createStudy(newStudy, loginUser);
        Locale locale = LocaleContextHolder.getLocale();
        flash.addFlashAttribute("msg", "new create study.");
        return "redirect:/study/" + savedStudy.getPath();
    }

    @GetMapping(path = "/{studyPath}")
    public String findStudyPage(@PathVariable(name = "studyPath") String path, @AuthUser UserDomain loginUser, Model model, RedirectAttributes flash) {
        // find study
        StudyDomain findStudy = studyService.findStudyWithPath(path);
        model.addAttribute("study", findStudy);
        model.addAttribute("user", loginUser);
        return "study/InformationPage";
    }

    @GetMapping(path = "/update/{studyPath}")
    public String updateStudyPage(@AuthUser UserDomain loginUser, @PathVariable(name = "studyPath") String path, Model model) {
        StudyDomain findStudy = studyService.findStudyWithPath(path);
        UpdateStudyForm updateForm = modelMapper.map(findStudy, UpdateStudyForm.class);
        model.addAttribute(updateForm);
        model.addAttribute("user", loginUser);
        return "study/updatePage";
    }

    @PostMapping(path = "/update/{studyPath}")
    public String updateStudyDo(@AuthUser UserDomain loginUser, @PathVariable(name = "studyPath") String path, @Validated UpdateStudyForm form, Errors errors, Model model, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            model.addAttribute("user", loginUser);
            return "study/UpdatePage";
        }
        flash.addFlashAttribute("msg", "update study done.");
        return "redirect:/study/" + path;
    }


    // user role check
    private boolean isAuth(UserDomain loginUser) {
        return loginUser.hasRole(UserRoleType.MANAGER, UserRoleType.ADMIN);
    }

    // user role check
    private boolean isAuth(UserDomain loginUser, StudyDomain study) {
        // user role check
        if (study.getOrganizer().equals(loginUser.getEmail()) || loginUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER)) {
            return true;
        }
        return false;
    }

}
