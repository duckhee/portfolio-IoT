package kr.co.won.blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.blog.api.resource.BlogCreateResource;
import kr.co.won.blog.api.resource.dto.BlogCreateResourceDto;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.form.CreateBlogForm;
import kr.co.won.blog.service.BlogService;
import kr.co.won.errors.resource.ValidErrorResource;
import kr.co.won.user.domain.UserDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(path = "/api/blogs")
@RequiredArgsConstructor
public class BlogApiController {

    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    /**
     * blog service
     */
    private final BlogService blogService;

    @GetMapping
    public ResponseEntity listBlogResources() {
        return null;
    }

    @PostMapping
    public ResponseEntity createBlogResources(@AuthUser UserDomain loginUser, @Validated @RequestBody CreateBlogForm form, Errors errors) {
        if (loginUser == null) {
            throw new AccessDeniedException("Not Login.");
        }
        if (errors.hasErrors()) {
            return validationResources(errors);
        }
        BlogDomain newBlog = modelMapper.map(form, BlogDomain.class);
        BlogDomain savedBlog = blogService.creatBlog(newBlog, loginUser);
        // mapped BlogResources
        BlogCreateResourceDto mappedNewBlog = modelMapper.map(savedBlog, BlogCreateResourceDto.class);
        // blog resource
        EntityModel<BlogCreateResourceDto> blogResource = BlogCreateResource.of(mappedNewBlog);
        // base hateoas controller link
        WebMvcLinkBuilder baseLink = linkTo(BlogApiController.class);
        // make creat uri
        URI createUri = baseLink.slash(savedBlog.getIdx()).toUri();
        // add blog hateoas link
        blogResource.add(baseLink.withRel("list-blogs"));

        blogResource.add(baseLink.slash(savedBlog.getIdx()).withRel("query-blogs"));
        blogResource.add(baseLink.slash(savedBlog.getIdx()).withRel("update-blogs"));
        blogResource.add(baseLink.slash(savedBlog.getIdx()).withRel("delete-blogs"));
        blogResource.add(Link.of("/docs/index.html#blog-create-resources", "profile"));
        return ResponseEntity.created(createUri).body(blogResource);
    }

    private ResponseEntity validationResources(Errors errors) {
        EntityModel<Errors> validationErrorResource = ValidErrorResource.of(errors);
        return ResponseEntity.badRequest().body(validationErrorResource);
    }

}
