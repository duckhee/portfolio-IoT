package kr.co.won.blog.controller;

import kr.co.won.auth.AuthUser;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.form.CreateBlogForm;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping(path = "/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final ModelMapper modelMapper;

    @GetMapping(path = "/create")
    public String createBlogPage(Model model) {
        model.addAttribute(new CreateBlogForm());
        return "blogs/createBlogPage";
    }

    @PostMapping(path = "/create")
    public String createBlogDo(@AuthUser UserDomain loginUser, @Validated CreateBlogForm form, Errors errors, Model model, RedirectAttributes flash) {
        if (loginUser == null) {
            flash.addFlashAttribute("msg", "Login First");
            return "redirect:/login";
        }
        if (errors.hasErrors()) {
            model.addAttribute("user", loginUser);
            return "blogs/createBlogPage";
        }
        BlogDomain newBlog = modelMapper.map(form, BlogDomain.class);
        // user project URI Setting
        newBlog.setWriter(loginUser.getName());
        newBlog.setWriterEmail(loginUser.getEmail());
        // todo flash message
        flash.addFlashAttribute("msg", "creat blog");
        return "redirect:/blogs/list";
    }

    @GetMapping(path = "/list")
    public String listBlogPage(PageDto page, Model model) {
        return "blogs/listBlogPage";
    }

}
