package kr.co.won.main;

import kr.co.won.blog.api.BlogApiController;
import kr.co.won.user.api.UserApiController;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class MainApiController {

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
}
