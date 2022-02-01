package kr.co.won.blog.api.resource.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import kr.co.won.blog.api.BlogApiController;
import kr.co.won.blog.api.BlogReplyApiController;
import kr.co.won.blog.domain.BlogReplyDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "replies")
public class ReplyResourceDto extends RepresentationModel<ReplyResourceDto> {

    private Long idx;

    private String replyer;

    private String replyContent;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public ReplyResourceDto(BlogReplyDomain reply) {
        this.idx = reply.getIdx();
        this.replyer = reply.getReplyer();
        this.replyContent = reply.getReplyContent();
        this.createdAt = reply.getCreatedAt();
        this.updatedAt = reply.getUpdatedAt();

        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(BlogReplyApiController.class, reply.getBlog().getIdx());
        this.add(linkBuilder.slash(this.idx).withSelfRel());
    }
}
