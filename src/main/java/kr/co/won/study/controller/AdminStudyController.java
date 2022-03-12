package kr.co.won.study.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.form.CreateStudyForm;
import kr.co.won.study.form.UpdateStudyForm;
import kr.co.won.study.service.StudyService;
import kr.co.won.study.validation.CreateStudyValidation;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.util.page.PageDto;
import kr.co.won.util.page.PageMaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/admin/study")
public class AdminStudyController {

    /**
     * Skip Mapping Null Value
     */
    @Resource(name = "skipModelMapper")
    private final ModelMapper modelMapper;

    /**
     * not skip null mapping
     */
    @Resource(name = "notSkipModelMapper")
    private final ModelMapper putModelMapper;

    private final ObjectMapper objectMapper;
    /**
     * study validation
     */
    private final CreateStudyValidation createStudyValidation;
    /**
     * study Service
     */
    private final StudyService studyService;

    /**
     * validation binding
     */
    @InitBinder(value = {"createStudyForm"})
    public void createStudyValidationBinding(WebDataBinder binder) {
        binder.addValidators(createStudyValidation);
    }

    @GetMapping(path = "/create")
    public String studyCreatePage(@AuthUser UserDomain authUser, Model model) {
        model.addAttribute(new CreateStudyForm());
        model.addAttribute("user", authUser);
        return "admin/study/studyCreatePage";
    }

    @PostMapping(path = "/create")
    public String studyCreateDo(@AuthUser UserDomain authUser, @Validated CreateStudyForm form, Errors errors, Model model, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            model.addAttribute("user", authUser);
            return "admin/study/studyCreatePage";
        }
        StudyDomain mappedStudy = modelMapper.map(form, StudyDomain.class);
//        mappedStudy.setOrganizer(authUser.getEmail());
        StudyDomain savedStudy = studyService.createStudy(mappedStudy, authUser);
        flash.addFlashAttribute("msg", savedStudy.getName() + " study create.");
        return "redirect:/admin/study/list";
    }

    @GetMapping(path = "/list")
    public String studyListPage(@AuthUser UserDomain authUser, PageDto pageDto, Model model) {
        Page pagingStudy = studyService.pagingStudy(pageDto);
        PageMaker pageMaker = new PageMaker<>(pagingStudy);
        model.addAttribute("page", pageMaker);
        return "admin/study/studyListPage";

    }

    @GetMapping(path = "/{studyPath}")
    public String studyInformationPage(@AuthUser UserDomain authUser, @PathVariable(name = "studyPath") String path, Model model) {
        StudyDomain findStudy = studyService.findStudyWithPath(path);
        model.addAttribute("study", findStudy);
        model.addAttribute("user", authUser);
        return "admin/study/studyInformationPage";
    }

    @GetMapping(path = "/update/{studyPath}")
    public String studyUpdatePage(@AuthUser UserDomain authUser, @PathVariable(name = "studyPath") String path, Model model) {
        // login user check auth and organizer check
        log.info("testing get path ::: {}", path);
        StudyDomain findStudy = studyService.findStudyWithPath(path);
        UpdateStudyForm updateStudyForm = modelMapper.map(findStudy, UpdateStudyForm.class);
        // update study information mapping update form
        model.addAttribute(updateStudyForm);
        model.addAttribute("user", authUser);
        return "admin/study/studyUpdatePage";
    }

    @PostMapping(path = "/update/{studyPath}")
    public String studyUpdateDo(@AuthUser UserDomain authUser, @PathVariable(name = "studyPath") String path, @Validated UpdateStudyForm form, Errors errors, Model model, RedirectAttributes flash) {
        // validation update
        if (errors.hasErrors()) {
            model.addAttribute("user", authUser);
            return "admin/study/studyUpdatePage";
        }
        StudyDomain mappedStudy = modelMapper.map(form, StudyDomain.class);
        StudyDomain updateStudy = studyService.updateStudy(path, mappedStudy, authUser);
        return "redirect:/admin/study/" + updateStudy.getPath();
    }

    @DeleteMapping(path = "/{studyIdx}")
    public ResponseEntity studyDeleteDo(@PathVariable(name = "studyIdx") Long studyIdx, @AuthUser UserDomain loginUser) {
        return null;
    }

    private boolean isAuth(UserDomain loginUser) {
        return loginUser.hasRole(UserRoleType.MANAGER, UserRoleType.ADMIN);
    }

    private boolean isAuth(UserDomain loginUser, StudyDomain study) {
        boolean isRole = loginUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER);
        return false;
    }

}
