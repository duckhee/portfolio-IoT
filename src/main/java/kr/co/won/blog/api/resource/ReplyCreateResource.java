package kr.co.won.blog.api.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import kr.co.won.blog.api.BlogReplyApiController;
import kr.co.won.blog.api.resource.dto.ReplyResourceDto;
import kr.co.won.blog.domain.BlogDomain;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

public class ReplyCreateResource extends EntityModel<ReplyCreateResource> {

    @JsonIgnore
    private BlogDomain blog;

    @JsonUnwrapped
    private ReplyResourceDto dto;

    public BlogDomain getBlog() {
        return this.blog;
    }

    public ReplyResourceDto getDto() {
        return this.dto;
    }


    public ReplyCreateResource(BlogDomain blog, ReplyResourceDto dto) {
        this.dto = dto;
        this.blog = blog;
    }


    public static EntityModel<ReplyResourceDto> of(BlogDomain blog, ReplyResourceDto dto) {
        List<Link> links = getSelfLink(blog, dto);
        return ReplyCreateResource.of(dto, links);
    }

    public static EntityModel<ReplyResourceDto> of(BlogDomain blog, ReplyResourceDto dto, String profile) {
        List<Link> links = getSelfLink(blog, dto);
        links.add(Link.of(profile, "profile").withType(HttpMethod.GET.name()));
        return ReplyCreateResource.of(dto, links);
    }


    private static List<Link> getSelfLink(BlogDomain blog, ReplyResourceDto dto) {
        WebMvcLinkBuilder linker = WebMvcLinkBuilder.linkTo(BlogReplyApiController.class, blog.getIdx());
        List<Link> links = new ArrayList<>();
        links.add(linker.slash(dto.getIdx()).withSelfRel().withType(HttpMethod.GET.name()));
        return links;
    }

}
