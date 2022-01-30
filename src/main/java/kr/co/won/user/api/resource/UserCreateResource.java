package kr.co.won.user.api.resource;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import kr.co.won.user.api.UserApiController;
import kr.co.won.user.api.resource.dto.UserCreateResourceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class UserCreateResource extends EntityModel<UserCreateResource> {

    @JsonUnwrapped
    private UserCreateResourceDto user;

    UserCreateResource(UserCreateResourceDto user) {
        this.user = user;
    }

    // user api link add
    public static WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(UserApiController.class);

    public static EntityModel<UserCreateResourceDto> of(UserCreateResourceDto user) {
        // get self links
        List<Link> links = getSelfLink(user);
        return UserCreateResource.of(user, links);
    }

    public static EntityModel<UserCreateResourceDto> of(UserCreateResourceDto user, String profile) {
        // get self links
        List<Link> links = getSelfLink(user);
        // profile link add
        links.add(Link.of(profile, "profile"));
        return UserCreateResource.of(user, links);
    }

    private static List<Link> getSelfLink(UserCreateResourceDto user) {
        List<Link> links = new ArrayList<>();
        // self link add
        links.add(linkBuilder.slash(user.getIdx()).withSelfRel());
        return links;
    }
}
