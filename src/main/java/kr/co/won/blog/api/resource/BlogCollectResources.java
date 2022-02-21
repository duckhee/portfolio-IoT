package kr.co.won.blog.api.resource;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import kr.co.won.blog.api.BlogApiController;
import kr.co.won.blog.api.BlogReplyApiController;
import kr.co.won.blog.api.resource.dto.BlogReadResourcesDto;
import kr.co.won.blog.api.resource.dto.ReplyResourceDto;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.user.domain.UserDomain;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BlogCollectResources extends CollectionModel<BlogReadResourcesDto> {

    @JsonUnwrapped
    private List<BlogReadResourcesDto> blogs;

    public BlogCollectResources(List<BlogDomain> blogs) {
        List<BlogReadResourcesDto> collectBlog = blogs.stream().map(BlogReadResourcesDto::new).collect(Collectors.toList());
        this.blogs = collectBlog;
    }


    public BlogCollectResources(List<BlogDomain> blogs, UserDomain authUser) {
        List<BlogReadResourcesDto> collectBlog = blogs.stream().map(blog -> new BlogReadResourcesDto(blog, authUser)).collect(Collectors.toList());
        this.blogs = collectBlog;
    }

    public static CollectionModel of(List<BlogDomain> blogs, UserDomain authUser) {
        List<BlogReadResourcesDto> collectBlog = blogs.stream().map(blog -> new BlogReadResourcesDto(blog, authUser)).collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        return BlogCollectResources.of(collectBlog, links);
    }

    public static CollectionModel of(List<BlogDomain> blogs, UserDomain authUser, String profile) {
        List<BlogReadResourcesDto> collectBlog = blogs.stream().map(blog -> new BlogReadResourcesDto(blog, authUser)).collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        return BlogCollectResources.of(collectBlog, links);
    }

    private static List<Link> getSelfLink(BlogDomain blog) {
        WebMvcLinkBuilder linker = WebMvcLinkBuilder.linkTo(BlogApiController.class);
        List<Link> links = new ArrayList<>();
        links.add(linker.slash(blog.getIdx()).withSelfRel().withType(HttpMethod.GET.name()));
        return links;
    }

}
