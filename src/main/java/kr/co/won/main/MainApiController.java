package kr.co.won.main;

import kr.co.won.auth.AuthBasicService;
import kr.co.won.auth.LoginUser;
import kr.co.won.blog.api.BlogApiController;
import kr.co.won.config.security.jwt.JwtTokenUtil;
import kr.co.won.main.dto.LoginFailedDto;
import kr.co.won.study.api.StudyApiController;
import kr.co.won.user.api.UserApiController;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class MainApiController {

    private final JwtTokenUtil jwtTokenUtil;

    private final AuthBasicService authBasicService;

    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity indexApiInformation() {
        var indexResource = new RepresentationModel<>();
        // make link
        WebMvcLinkBuilder userLinker = linkTo(UserApiController.class);
        WebMvcLinkBuilder blogLinker = linkTo(BlogApiController.class);
        WebMvcLinkBuilder studyLinker = linkTo(StudyApiController.class);
        // add link
        indexResource.add(userLinker.withRel("users"));
        indexResource.add(blogLinker.withRel("blogs"));
        indexResource.add(studyLinker.withRel("studies"));
        return ResponseEntity.ok().body(indexResource);
    }

    @GetMapping(path = "/auth-login")
    public ResponseEntity jwtTokenLoginFailedResource() {
        // get user locale information
        Locale locale = LocaleContextHolder.getLocale();
        // login failed information dto
        LoginFailedDto loginFailedDto = new LoginFailedDto();
        loginFailedDto.setLoginUri(URI.create("/api/auth-login"));
        loginFailedDto.setMsg(messageSource.getMessage("failed.login", null, "로그인 정보를 확인해주시기 바랍니다.", locale));
        // login failed resource
        RepresentationModel<?> failedLoginJwt = RepresentationModel.of(loginFailedDto);
        // login failed reosurce add link
        failedLoginJwt.add(linkTo(methodOn(MainApiController.class).jwtTokenLoginResource(new LoginForm())).withRel("login-uri"));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(failedLoginJwt);
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
