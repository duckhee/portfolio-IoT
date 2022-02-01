package kr.co.won.blog.controller;

import kr.co.won.auth.AuthUser;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.form.CreateBlogForm;
import kr.co.won.blog.service.BlogService;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

@Slf4j
@Controller
@RequestMapping(path = "/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final ModelMapper modelMapper;

    @Resource(name = "blogService")
    private final BlogService blogService;

    @GetMapping(path = "/create")
    public String createBlogPage(Model model) {
        model.addAttribute(new CreateBlogForm());
        return "blogs/createBlogPage";
    }

    //    @Secured(value = {"ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER"})
    @PostMapping(path = "/create")
    public String createBlogDo(@AuthUser UserDomain loginUser, @Validated CreateBlogForm form, Errors errors, Model model, RedirectAttributes flash) {
        if (loginUser == null) {
            flash.addFlashAttribute("msg", "Login First");
            return "redirect:/login";
        }
        //validation check
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
        Page pagingResult = blogService.pagingBlog(page);
        model.addAttribute("page", pagingResult);
        return "blogs/listBlogPage";
    }

    @GetMapping(path = "/{idx}")
    public String readBlogPage(@PathVariable(name = "idx") Long blogIdx) {
        return "";
    }

    @GetMapping(path = "/update")
    public String updateBlogPage(@RequestParam(name = "blog") Long blog, Model model) {
        return "";
    }

    @PostMapping(path = "/update")
    public String updateBlogDo(@AuthUser UserDomain loginUser, @RequestParam(name = "blog") Long blog, Model model, RedirectAttributes flash) {
        return "";
    }

    @DeleteMapping(path = "/delete/{blogIdx}")
    public String deleteBlogDo(@AuthUser UserDomain loginUser, @PathVariable(name = "blogIdx") Long blogIdx, RedirectAttributes flash) {
        return "";
    }
}
