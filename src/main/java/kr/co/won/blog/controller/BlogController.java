package kr.co.won.blog.controller;

import kr.co.won.auth.AuthUser;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.domain.BlogReplyDomain;
import kr.co.won.blog.form.BlogForm;
import kr.co.won.blog.service.BlogService;
import kr.co.won.user.domain.UserDomain;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.List;

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
        model.addAttribute(new BlogForm());
        return "blogs/createBlogPage";
    }

    //    @Secured(value = {"ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER"})
    @PostMapping(path = "/create")
    public String createBlogDo(@AuthUser UserDomain loginUser, @Validated BlogForm form, Errors errors, Model model, RedirectAttributes flash) {
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
        log.info("model mapper ::: {}", newBlog);
        BlogDomain savedBlog = blogService.createBlog(newBlog, loginUser);
        // todo flash message
        flash.addFlashAttribute("msg", "creat blog");
        return "redirect:/blogs/list";
    }

    @GetMapping(path = "/list")
    public String listBlogPage(PageDto page, Model model) {
        Page pagingResult = blogService.pagingBlog(page);
        PageMaker paging = new PageMaker<>(pagingResult);
        model.addAttribute("page", paging);
        return "blogs/listBlogPage";
    }

    @GetMapping(path = "/{idx}")
    public String readBlogPage(@PathVariable(name = "idx") Long blogIdx, Model model) {
        BlogDomain findBlog = blogService.readBlog(blogIdx);
        List<BlogReplyDomain> findBlogReplies = blogService.listReply(blogIdx);
        model.addAttribute("blog", findBlog);
        model.addAttribute("replies", findBlogReplies);
        return "blogs/informationBlogPage";

    }

    @GetMapping(path = "/update")
    public String updateBlogPage(@AuthUser UserDomain loginUser, @RequestParam(name = "blog") Long blogIdx, Model model) {
        BlogDomain findBlog = blogService.readBlog(blogIdx);
        List<BlogReplyDomain> findBlogReplies = blogService.listReply(blogIdx);
        model.addAttribute("blogForm", findBlog);
        model.addAttribute("replies", findBlogReplies);
        model.addAttribute("user", loginUser);
        return "blogs/updateBlogPage";
    }

    @PostMapping(path = "/update")
    public String updateBlogDo(@AuthUser UserDomain loginUser, @RequestParam(name = "blog") Long blogIdx, @Validated BlogForm form, Errors errors, Model model, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            List<BlogReplyDomain> findBlogReplies = blogService.listReply(blogIdx);
            model.addAttribute("replies", findBlogReplies);
            model.addAttribute("user", loginUser);
            return "blogs/updateBlogPage";
        }
        BlogDomain updateBlog = modelMapper.map(form, BlogDomain.class);
        blogService.updateBlog(blogIdx, updateBlog, loginUser);
        return "redirect:/blogs/" + blogIdx;
    }

    @DeleteMapping
    public ResponseEntity deleteListBlogDo(List<Long> blogIdxes) {
        return null;
    }

    @DeleteMapping(path = "/delete/{blogIdx}")
    public ResponseEntity deleteBlogDo(@AuthUser UserDomain loginUser, @PathVariable(name = "blogIdx") Long blogIdx, RedirectAttributes flash) {
        return null;
    }
}
