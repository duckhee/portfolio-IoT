package kr.co.won.blog.api.resource.dto;

import kr.co.won.blog.api.BlogApiController;
import kr.co.won.blog.domain.BlogDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

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


}
