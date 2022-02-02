package kr.co.won.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.blog.form.CreateBlogForm;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(path = "/admin/blogs")
@RequiredArgsConstructor
public class AdminBlogController {

    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;


    @GetMapping(path = "/create")
    public String createBlogPage(Model model) {
        model.addAttribute(new CreateBlogForm());
        return "admin/blogs/createBlogPage";
    }

    @PostMapping(path = "/create")
    public String createBlogDo(Model model, @Validated CreateBlogForm form, Errors errors, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            return "admin/blogs/createBlogPage";
        }
        return "redirect:/admin/blogs/list";
    }

    @GetMapping(path = "/list")
    public String listBlogPage(PageDto pageDto, Model model) {
        return "admin/blogs/listBlogPage";
    }

    @GetMapping(path = "/{idx}")
    public String readBlogPage(@PathVariable(name = "idx") Long blogIdx, Model model) {
        return "admin/blogs/informationBlogPage";
    }

}
