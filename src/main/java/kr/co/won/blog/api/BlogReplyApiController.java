package kr.co.won.blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.blog.api.resource.ReplyCreateResource;
import kr.co.won.blog.api.resource.dto.ReplyResourceDto;
import kr.co.won.blog.domain.BlogReplyDomain;
import kr.co.won.blog.form.CreateReplyForm;
import kr.co.won.blog.service.BlogService;
import kr.co.won.errors.resource.ValidErrorResource;
import kr.co.won.user.domain.UserDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/blogs/{blogIdx}/reply")
@RequiredArgsConstructor
public class BlogReplyApiController {

    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    /**
     * blog service
     */
    private final BlogService blogService;


    @GetMapping
    public ResponseEntity listBlogRepliesResource(@PathVariable(name = "blogIdx") Long blogIdx) {
        return null;
    }

    @PostMapping
    public ResponseEntity createBlogRepliesResource(@AuthUser UserDomain loginUser, @PathVariable(name = "blogIdx") Long blogIdx, @Validated @RequestBody CreateReplyForm form, Errors errors) {
        if (loginUser == null) {
            throw new AccessDeniedException("login first.");
        }
        if (errors.hasErrors()) {
            return replyValidationErrorResource(errors);
        }
        BlogReplyDomain newReply = BlogReplyDomain.builder()
                .replyContent(form.getReplyContent())
                .build();
        BlogReplyDomain savedReply = blogService.createReply(blogIdx, newReply, loginUser);
        ReplyResourceDto mappedResource = modelMapper.map(savedReply, ReplyResourceDto.class);
        EntityModel<ReplyResourceDto> resultResource = ReplyCreateResource.of(savedReply.getBlog(), mappedResource);
        return ResponseEntity.ok().body(resultResource);
    }


    @GetMapping(path = "/{replyIdx}")
    public ResponseEntity readBlogRepliesResource(@PathVariable(name = "blogIdx") Long blogIdx, @PathVariable(name = "replyIdx") Long replyIdx) {
        return null;
    }

    @DeleteMapping(path = "/{replyIdx}")
    public ResponseEntity deleteBlogRepliesResource(@PathVariable(name = "blogIdx") Long blogIdx, @PathVariable(name = "replyIdx") Long replyIdx) {
        return null;
    }

    // valid error resource
    private ResponseEntity replyValidationErrorResource(Errors errors) {
        EntityModel<Errors> validErrorResource = ValidErrorResource.of(errors);
        return ResponseEntity.badRequest().body(validErrorResource);
    }
}
