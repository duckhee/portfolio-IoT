package kr.co.won.study.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.form.CreateStudyForm;
import kr.co.won.study.validation.CreateStudyValidation;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(path = "/admin/study")
@RequiredArgsConstructor
public class AdminStudyController {

    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    private final CreateStudyValidation createStudyValidation;

    @InitBinder(value = {"createStudyForm"})
    public void createStudyValidationBinding(WebDataBinder binder) {
        binder.addValidators(createStudyValidation);
    }

    @GetMapping(path = "/create")
    public String studyCreatePage(@AuthUser UserDomain authUser, Model model) {
        return "admin/study/studyCreatePage";
    }

    @PostMapping(path = "/create")
    public String studyCreateDo(@AuthUser UserDomain authUser, @Validated CreateStudyForm form, Errors errors, Model model, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            return "admin/study/studyCreatePage";
        }
        return "redirect:/admin/study/list";
    }

    @GetMapping(path = "/list")
    public String studyListPage(@AuthUser UserDomain authUser, PageDto pageDto, Model model) {
        return "admin/study/studyListPage";

    }

    @GetMapping(path = "/{studyPath}")
    public String studyInformationPage(@AuthUser UserDomain authUser, @PathVariable(name = "studyPath") String path, Model model) {
        return "admin/study/studyInformationPage";
    }

    @GetMapping(path = "/update")
    public String studyUpdatePage(@AuthUser UserDomain authUser, @RequestParam(name = "studyPath") String pat, Model model) {
        return "admin/study/studyInformationPage";
    }

    @PostMapping(path = "/update")
    public String studyUpdateDo(@AuthUser UserDomain authUser, @RequestParam(name = "studyPath") String path, Model model, RedirectAttributes flash) {
        return "redirect:/admin/study/" + path;
    }

    @DeleteMapping(path = "/{studyIdx}")
    public ResponseEntity studyDeleteDo(@PathVariable(name = "studyIdx") Long studyIdx, @AuthUser UserDomain loginUser) {
        return null;
    }

    private boolean isAuth(UserDomain loginUser) {
        return loginUser.hasRole(UserRoleType.MANAGER, UserRoleType.ADMIN);
    }

    private boolean isAuth(UserDomain loginUser, StudyDomain study) {
        return false;
    }

}
