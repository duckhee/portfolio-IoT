package kr.co.won.blog.api.resource.dto;

import kr.co.won.blog.api.BlogApiController;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// if this class list change wrap name blogs
@Relation(collectionRelation = "blogs")
public class BlogReadResourcesDto extends RepresentationModel<BlogReadResourcesDto> {

    private Long idx;

    private String title;

    private String content;

    private String writer;

    private String writerEmail;

    private URI projectUri;

    private Long viewCnt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<ReplyResourceDto> replies;

    public BlogReadResourcesDto(BlogDomain blog) {
        this.idx = blog.getIdx();
        this.title = blog.getTitle();
        this.content = blog.getContent();
        this.writer = blog.getWriter();
        this.writerEmail = blog.getWriterEmail();
        this.viewCnt = blog.getViewCnt();
        this.projectUri = (blog.getProjectUrl() != null) ? URI.create(blog.getProjectUrl()) : null;
        this.createdAt = blog.getCreatedAt();
        this.updatedAt = blog.getUpdatedAt();
        this.replies = blog.getReplies().stream().map(reply -> new ReplyResourceDto(reply)).collect(Collectors.toList());

        this.add(WebMvcLinkBuilder.linkTo(BlogApiController.class).slash(this.idx).withSelfRel());
    }

    public BlogReadResourcesDto(BlogDomain blog, UserDomain authUser) {
        this.idx = blog.getIdx();
        this.title = blog.getTitle();
        this.content = blog.getContent();
        this.writer = blog.getWriter();
        this.writerEmail = blog.getWriterEmail();
        this.viewCnt = blog.getViewCnt();
        this.projectUri = (blog.getProjectUrl() != null) ? URI.create(blog.getProjectUrl()) : null;
        this.createdAt = blog.getCreatedAt();
        this.updatedAt = blog.getUpdatedAt();
        this.replies = blog.getReplies().stream().map(reply -> new ReplyResourceDto(reply, authUser)).collect(Collectors.toList());

        this.add(WebMvcLinkBuilder.linkTo(BlogApiController.class).slash(this.idx).withSelfRel());
        this.add(WebMvcLinkBuilder.linkTo(BlogApiController.class).slash(this.idx).withRel("query-blogs").withType(HttpMethod.GET.name()));
        if(authUser.hasRole(UserRoleType.ADMIN , UserRoleType.MANAGER) || blog.getWriterEmail().equals(authUser.getEmail())){
            this.add(WebMvcLinkBuilder.linkTo(BlogApiController.class).slash(this.idx).withRel("update-blogs").withType(HttpMethod.PUT.name()));
            this.add(WebMvcLinkBuilder.linkTo(BlogApiController.class).slash(this.idx).withRel("delete-blogs").withType(HttpMethod.DELETE.name()));
        }
    }

}
