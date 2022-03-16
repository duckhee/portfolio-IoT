package kr.co.won.main;

import kr.co.won.auth.AuthBasicService;
import kr.co.won.auth.LoginUser;
import kr.co.won.blog.api.BlogApiController;
import kr.co.won.config.security.jwt.JwtTokenUtil;
import kr.co.won.user.api.UserApiController;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class MainApiController {

    private final JwtTokenUtil jwtTokenUtil;

    private final AuthBasicService authBasicService;

    @GetMapping
    public ResponseEntity indexApiInformation() {
        var indexResource = new RepresentationModel<>();
        // make link
        WebMvcLinkBuilder userLinker = linkTo(UserApiController.class);
        WebMvcLinkBuilder blogLinker = linkTo(BlogApiController.class);
        // add link
        indexResource.add(userLinker.withRel("users"));
        indexResource.add(blogLinker.withRel("blogs"));
        return ResponseEntity.ok().body(indexResource);
    }

    @PostMapping(path = "/auth-login")
    public ResponseEntity jwtTokenLoginResource(@RequestBody LoginForm form) {
        LoginUser user = authBasicService.jwtLogin(form.email, form.getPassword());
        String token = jwtTokenUtil.generateToken(user.getUser().getEmail());
        return ResponseEntity.ok().body(token);
    }

    @Data
    static class LoginForm {
        private String email;
        private String password;
    }
}
