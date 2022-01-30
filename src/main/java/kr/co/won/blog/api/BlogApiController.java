package kr.co.won.blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.blog.form.CreateBlogForm;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity createBlogResources(@AuthUser UserDomain loginUser, @Validated CreateBlogForm form, Errors errors) {
        if(loginUser == null){
            throw new AccessDeniedException("Not Login.");
        }
        if (errors.hasErrors()) {
            return validationResources(errors);
        }
        return null;
    }

    private ResponseEntity validationResources(Errors errors) {
        EntityModel<Errors> validationErrorResource = ValidErrorResource.of(errors);
        return ResponseEntity.badRequest().body(validationErrorResource);
    }

}
