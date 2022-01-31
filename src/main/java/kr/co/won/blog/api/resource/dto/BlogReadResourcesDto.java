package kr.co.won.blog.api.resource.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.won.blog.api.BlogApiController;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.domain.BlogReplyDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.EntityMode;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.retry.annotation.Backoff;

import javax.swing.text.html.parser.Entity;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
        this.projectUri = (blog.getProjectUri() != null) ? URI.create(blog.getProjectUri()) : null;
        this.createdAt = blog.getCreatedAt();
        this.updatedAt = blog.getUpdatedAt();
        this.replies = blog.getReplies().stream().map(reply -> new ReplyResourceDto(reply)).collect(Collectors.toList());

        this.add(WebMvcLinkBuilder.linkTo(BlogApiController.class).slash(this.idx).withSelfRel());
    }


}
