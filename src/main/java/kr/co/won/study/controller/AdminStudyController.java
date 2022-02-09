package kr.co.won.study.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(path = "/admin/study")
@RequiredArgsConstructor
public class AdminStudyController {

    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;


    @GetMapping(path = "/create")
    public String studyCreatePage(@AuthUser UserDomain authUser, Model model) {
        return "admin/study/studyCreatePage";
    }

    @PostMapping(path = "/create")
    public String studyCreateDo(@AuthUser UserDomain authUser, Model model, RedirectAttributes flash) {
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

    @GetMapping(path = "/{studyPath}/update")
    public String studyUpdatePage(@AuthUser UserDomain authUser, @PathVariable(name = "studyPath") String pat, Model model) {
        return "admin/study/studyInformationPage";
    }

    @PostMapping(path = "/{studyPath}/update")
    public String studyUpdateDo(@AuthUser UserDomain authUser, @PathVariable(name = "studyPath") String path, Model model, RedirectAttributes flash) {
        return "redirect:/admin/study/" + path;
    }

}
