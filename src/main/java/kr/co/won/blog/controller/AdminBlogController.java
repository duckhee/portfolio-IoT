package kr.co.won.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.domain.BlogReplyDomain;
import kr.co.won.blog.form.CreateBlogForm;
import kr.co.won.blog.service.BlogService;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import kr.co.won.util.page.PageMaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(path = "/admin/blogs")
@RequiredArgsConstructor
public class AdminBlogController {

    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    private final BlogService blogService;

    @GetMapping(path = "/create")
    public String createBlogPage(Model model) {
        model.addAttribute(new CreateBlogForm());
        return "admin/blogs/createBlogPage";
    }

    @PostMapping(path = "/create")
    public String createBlogDo(@AuthUser UserDomain loginUser, Model model, @Validated CreateBlogForm form, Errors errors, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            return "admin/blogs/createBlogPage";
        }
        BlogDomain mappedBlog = modelMapper.map(form, BlogDomain.class);
        // set writer login user
        mappedBlog.setWriter(loginUser.getName());
        mappedBlog.setWriterEmail(loginUser.getEmail());
        log.info("get mapping blog content ::: {}", mappedBlog.getContent());
        // blog service
        BlogDomain savedBlog = blogService.createBlog(mappedBlog);
        log.info("save blog content ::: {}", savedBlog.getContent());
        // add flash message
        flash.addFlashAttribute("msg", savedBlog.getTitle() + " blog saved.");
        return "redirect:/admin/blogs/list";
    }

    @GetMapping(path = "/list")
    public String listBlogPage(PageDto pageDto, Model model) {
        Page pageResult = blogService.pagingListBlog(pageDto);
        PageMaker pagingResult = new PageMaker<>(pageResult);

        model.addAttribute("page", pagingResult);
        return "admin/blogs/listBlogPage";
    }

    @GetMapping(path = "/{idx}")
    public String readBlogPage(@PathVariable(name = "idx") Long blogIdx, Model model) {
        BlogDomain findBlog = blogService.readBlog(blogIdx);
        List<BlogReplyDomain> findBlogReplies = blogService.listReply(blogIdx);
        model.addAttribute("blog", findBlog);
        model.addAttribute("replies", findBlogReplies);
        return "admin/blogs/informationBlogPage";
    }

    @GetMapping(path = "/update")
    public String updateBlogPage(@RequestParam(name = "blog") Long blogIdx, Model model) {
        return "";
    }

    @PostMapping(path = "/update")
    public String updateBlogDo(@RequestParam(name = "blog") Long blogIdx, Model model, RedirectAttributes flash) {
        return "";
    }

}
