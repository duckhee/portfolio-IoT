package kr.co.won.blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.blog.api.resource.ReplyCreateResource;
import kr.co.won.blog.api.resource.ReplyCollectResources;
import kr.co.won.blog.api.resource.dto.ReplyResourceDto;
import kr.co.won.blog.domain.BlogReplyDomain;
import kr.co.won.blog.form.CreateReplyForm;
import kr.co.won.blog.service.BlogService;
import kr.co.won.errors.resource.ValidErrorResource;
import kr.co.won.user.domain.UserDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
    public ResponseEntity listBlogRepliesResource(@AuthUser UserDomain authUser, @PathVariable(name = "blogIdx") Long blogIdx) {
        List<BlogReplyDomain> getReplies = blogService.listReply(blogIdx);
        // make reply
        CollectionModel<ReplyResourceDto> resourceDtos = ReplyCollectResources.of(getReplies, authUser);

        // add profile link
        resourceDtos.add(Link.of("/docs/index.html#replies-list-resources", "profile"));
        return ResponseEntity.ok().body(resourceDtos);
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
        // create Location Uri
        URI createUri = URI.create("/api/blogs/" + blogIdx + "/reply/" + savedReply.getIdx());
        // make createReply Resource add blog reply resource link
        EntityModel<ReplyResourceDto> resultResource = ReplyCreateResource.of(savedReply.getBlog(), mappedResource);
        resultResource.add(Link.of("/docs/index.html#replies-create-resources", "profile"));
        return ResponseEntity.created(createUri).body(resultResource);
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
