package kr.co.won.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.blog.domain.BlogReplyDomain;
import kr.co.won.blog.dto.ReplyListDto;
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
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity createReply(@AuthUser UserDomain loginUser, @PathVariable(value = "blogIdx") Long blogIdx, @Validated @RequestBody CreateReplyForm form, Errors errors) {
        log.info("get create reply ::: {}, form ::: {}", loginUser, form);
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        BlogReplyDomain mappedReply = modelMapper.map(form, BlogReplyDomain.class);
        BlogReplyDomain savedReply = blogService.createReply(blogIdx, mappedReply, loginUser);
        URI createUri = URI.create("/blogs/" + blogIdx + "/reply/" + savedReply.getIdx());
        return ResponseEntity.created(createUri).body(savedReply.getIdx());
    }

    @GetMapping
    public ResponseEntity listReplies(@PathVariable(value = "blogIdx") Long idx) {
        List<BlogReplyDomain> findBlogReplies = blogService.listReply(idx);
        List<ReplyListDto> blogReplies = findBlogReplies.stream().map(ReplyListDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(blogReplies);
    }

    @PutMapping(path = "/update/{replyIdx}")
    public ResponseEntity updateReplies(@AuthUser UserDomain loginUser, @PathVariable(value = "blogIdx") Long blogIdx, @PathVariable(value = "replyIdx") Long replyIdx) {
        return null;
    }

    @PutMapping(path = "/{replyIdx}")
    public ResponseEntity updateRepliesUsingPutMethod(@AuthUser UserDomain loginUser, @PathVariable(value = "blogIdx") Long blogIdx, @PathVariable(value = "replyIdx") Long replyIdx) {
        return null;
    }

    @PostMapping(path = "/delete/{replyIdx}")
    public ResponseEntity removeReplies(@AuthUser UserDomain loginUser, @PathVariable(value = "blogIdx") Long blogIdx, @PathVariable(value = "replyIdx") Long replyIdx) {
        BlogReplyDomain blogReplyDomain = blogService.removeReply(blogIdx, replyIdx, loginUser);
        if (blogReplyDomain == null) {
            return ResponseEntity.badRequest().body("impossible delete reply");
        }
        return ResponseEntity.ok().body("deleted");
    }


    @DeleteMapping(path = "/{replyIdx}")
    public ResponseEntity removeRepliesUsingDeleteMethod(@AuthUser UserDomain loginUser, @PathVariable(value = "blogIdx") Long blogIdx, @PathVariable(value = "replyIdx") Long replyIdx) {
        BlogReplyDomain blogReplyDomain = blogService.removeReply(blogIdx, replyIdx, loginUser);
        if (blogReplyDomain == null) {
            return ResponseEntity.badRequest().body("impossible delete reply");
        }
        return ResponseEntity.ok().body("deleted");
    }

}
