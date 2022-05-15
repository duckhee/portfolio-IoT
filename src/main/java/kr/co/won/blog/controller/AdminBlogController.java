package kr.co.won.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.domain.BlogReplyDomain;
import kr.co.won.blog.form.BlogForm;
import kr.co.won.blog.service.BlogService;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.type.UserRoleType;
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

import java.util.ArrayList;
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
        model.addAttribute(new BlogForm());
        return "admin/blogs/createBlogPage";
    }

    @PostMapping(path = "/create")
    public String createBlogDo(@AuthUser UserDomain loginUser, Model model, @Validated BlogForm form, Errors errors, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            return "admin/blogs/createBlogPage";
        }
        BlogDomain mappedBlog = modelMapper.map(form, BlogDomain.class);
        // set writer login user
        mappedBlog.setWriter(loginUser.getName());
        mappedBlog.setWriterEmail(loginUser.getEmail());
        List<String> getResourceNameList = new ArrayList<>();
        // blog have resource image have
        if (form.getResources() != null) {
            getResourceNameList = List.of(form.getResources().split(","));
        }
        // blog service
        BlogDomain savedBlog = blogService.createBlogMapResource(mappedBlog, getResourceNameList);

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
    public String readBlogPage(@AuthUser UserDomain loginUser, @PathVariable(name = "idx") Long blogIdx, Model model) {
        BlogDomain findBlog = blogService.readBlog(blogIdx);
        List<BlogReplyDomain> findBlogReplies = blogService.listReply(blogIdx);
        model.addAttribute("user", loginUser);
        model.addAttribute("blog", findBlog);
        model.addAttribute("replies", findBlogReplies);
        return "admin/blogs/informationBlogPage";
    }

    @GetMapping(path = "/update")
    public String updateBlogPage(@AuthUser UserDomain authUser, @RequestParam(name = "blog") Long blogIdx, Model model, RedirectAttributes flash) {
        BlogDomain findBlog = blogService.readBlog(blogIdx);
        if (!isAuth(authUser, findBlog)) {
            flash.addFlashAttribute("msg", "not have auth.");
            return "redirect:/admin/blogs/" + blogIdx;
        }
        BlogForm mappedForm = modelMapper.map(findBlog, BlogForm.class);
        // set form data
        model.addAttribute("blogForm", mappedForm);
        return "admin/blogs/updateBlogPage";
    }


    @PostMapping(path = "/update")
    public String updateBlogDo(@AuthUser UserDomain authUser, @RequestParam(name = "blog") Long blogIdx, @Validated BlogForm form, Errors errors, Model model, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            return "admin/blogs/updateBlogPage";
        }
        BlogDomain mappedUpdateForm = modelMapper.map(form, BlogDomain.class);
        BlogDomain updateBlog = blogService.updateBlog(blogIdx, mappedUpdateForm, authUser);
        flash.addFlashAttribute("msg", updateBlog.getTitle() + " blog Update.");
        return "redirect:/admin/blogs/list";
    }

    @DeleteMapping(path = "/{idx}")
    public ResponseEntity deleteBlogDo(@AuthUser UserDomain authUser, @PathVariable(name = "idx") Long blogIdx) {
        if (!isAuth(authUser)) {
            return ResponseEntity.badRequest().build();
        }
            blogService.deleteBlog(blogIdx, authUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity bulkDeleteBlogDo(@AuthUser UserDomain authUser, @RequestBody List<Long> blogIdxes) {
        if (!isAuth(authUser)) {
            return ResponseEntity.badRequest().build();
        }
        blogService.bulkDeleteBlogs(blogIdxes, authUser);
        return ResponseEntity.ok().build();
    }

    // user role check
    private boolean isAuth(UserDomain authUser) {
        return authUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER);
    }

    // user auth check
    private boolean isAuth(UserDomain authUser, BlogDomain findBlog) {
        return authUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER) || findBlog.isOwner(authUser.getEmail());
    }

}
