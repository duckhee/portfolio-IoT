package kr.co.won.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.blog.domain.BlogReplyDomain;
import kr.co.won.blog.form.CreateReplyForm;
import kr.co.won.blog.service.BlogService;
import kr.co.won.user.domain.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * reply function using ajax
 */

@Slf4j
@RestController
@RequestMapping(path = "/blogs/{blogIdx}/reply")
@RequiredArgsConstructor
public class BlogReplyController {

    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    private final BlogService blogService;

    @PostMapping
    public ResponseEntity createReply(@AuthUser UserDomain loginUser, @Validated @RequestBody CreateReplyForm form, Errors errors) {
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        BlogReplyDomain mappedReply = modelMapper.map(form, BlogReplyDomain.class);
        BlogReplyDomain savedReply = blogService.createReply(form.getBlogIdx(), mappedReply, loginUser);
        return ResponseEntity.ok().body(savedReply);
    }

}
