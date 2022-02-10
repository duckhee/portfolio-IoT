package kr.co.won.blog.api.resource.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import kr.co.won.blog.api.BlogApiController;
import kr.co.won.blog.api.BlogReplyApiController;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.domain.BlogReplyDomain;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.rest.core.mapping.HttpMethods;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;

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
        this.add(linkBuilder.slash(this.idx).withRel("query-reply").withType(HttpMethod.GET.name()));
        this.add(linkBuilder.slash(this.idx).withRel("update-reply").withType(HttpMethod.PUT.name()));
        this.add(linkBuilder.slash(this.idx).withRel("delete-reply").withType(HttpMethod.DELETE.name()));
    }


    public ReplyResourceDto(BlogReplyDomain reply, UserDomain authUser) {
        this.idx = reply.getIdx();
        this.replyer = reply.getReplyer();
        this.replyContent = reply.getReplyContent();
        this.createdAt = reply.getCreatedAt();
        this.updatedAt = reply.getUpdatedAt();

        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(BlogReplyApiController.class, reply.getBlog().getIdx());
        this.add(linkBuilder.slash(this.idx).withSelfRel());
        this.add(linkBuilder.slash(this.idx).withRel("query-reply").withType(HttpMethod.GET.name()));
        // extra link
        if (isAuth(reply, authUser)) {
            this.add(linkBuilder.slash(this.idx).withRel("update-reply").withType(HttpMethod.PUT.name()));
            this.add(linkBuilder.slash(this.idx).withRel("delete-reply").withType(HttpMethod.DELETE.name()));
        }

    }


    public ReplyResourceDto(BlogReplyDomain reply, BlogDomain blog, UserDomain authUser) {
        this.idx = reply.getIdx();
        this.replyer = reply.getReplyer();
        this.replyContent = reply.getReplyContent();
        this.createdAt = reply.getCreatedAt();
        this.updatedAt = reply.getUpdatedAt();

        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(BlogReplyApiController.class, reply.getBlog().getIdx());
        if (isAuth(blog, reply, authUser)) {
            this.add(linkBuilder.slash(this.idx).withRel("update-reply").withType(HttpMethod.PUT.name()));
            this.add(linkBuilder.slash(this.idx).withRel("delete-reply").withType(HttpMethod.DELETE.name()));
        }
        this.add(linkBuilder.slash(this.idx).withSelfRel());
        this.add(linkBuilder.slash(this.idx).withRel("query-reply").withType(HttpMethod.GET.name()));

    }

    /**
     * User Auth checking
     */

    private boolean isAuth(BlogReplyDomain reply, UserDomain authUser) {
        return authUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER) || reply.getReplyerEmail().equals(authUser.getEmail());
    }

    private boolean isAuth(BlogDomain blog, BlogReplyDomain reply, UserDomain authUser) {
        return authUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER) || reply.getReplyerEmail().equals(authUser.getEmail()) || blog.isOwner(authUser.getEmail());
    }

}
