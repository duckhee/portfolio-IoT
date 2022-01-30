package kr.co.won.blog.api.resource;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import kr.co.won.blog.api.BlogApiController;
import kr.co.won.blog.api.resource.dto.BlogCreateResourceDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.ArrayList;
import java.util.List;

public class BlogCreateResource extends EntityModel<BlogCreateResource> {

    @JsonUnwrapped
    private BlogCreateResourceDto dto;

    BlogCreateResource(BlogCreateResourceDto dto) {
        this.dto = dto;
    }

    public BlogCreateResourceDto getDto() {
        return dto;
    }

    private static WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(BlogApiController.class);

    public static EntityModel<BlogCreateResourceDto> of(BlogCreateResourceDto blog) {
        List<Link> links = getSelfLink(blog);
        return BlogCreateResource.of(blog, links);
    }

    private static EntityModel<BlogCreateResourceDto> of(BlogCreateResourceDto blog, String profile) {
        List<Link> links = getSelfLink(blog);
        links.add(Link.of(profile, "profile"));
        return BlogCreateResource.of(blog, links);
    }

    private static List<Link> getSelfLink(BlogCreateResourceDto blog) {
        List<Link> links = new ArrayList<>();
        links.add(linkBuilder.slash(blog.getIdx()).withSelfRel());
        return links;
    }
}
