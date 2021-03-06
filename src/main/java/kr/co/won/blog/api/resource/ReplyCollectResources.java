package kr.co.won.blog.api.resource;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import kr.co.won.blog.api.BlogReplyApiController;
import kr.co.won.blog.api.resource.dto.ReplyResourceDto;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.domain.BlogReplyDomain;
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
public class ReplyCollectResources extends CollectionModel<ReplyCollectResources> {

    @JsonUnwrapped
    private List<ReplyResourceDto> replies;

    public ReplyCollectResources(List<BlogReplyDomain> replies) {
        List<ReplyResourceDto> list = new ArrayList<>();
        replies.forEach(reply -> list.add(new ReplyResourceDto(reply)));
        this.replies = list;
    }

    public ReplyCollectResources(List<BlogReplyDomain> replies, UserDomain authUser) {
        List<ReplyResourceDto> list = new ArrayList<>();
        replies.forEach(reply -> list.add(new ReplyResourceDto(reply, authUser)));
        this.replies = list;
    }

    public static CollectionModel of(List<BlogReplyDomain> replies, UserDomain authUser) {
//        List<ReplyResourceDto> list = new ArrayList<>();
//        replies.forEach(reply -> list.add(new ReplyResourceDto(reply, authUser)));
        List<ReplyResourceDto> collect = replies.stream().map(reply -> new ReplyResourceDto(reply, authUser)).collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        return ReplyCollectResources.of(collect, links);
    }

    public static CollectionModel of(List<BlogReplyDomain> replies, UserDomain authUser, String profile) {
//        List<ReplyResourceDto> list = new ArrayList<>();
//        replies.forEach(reply -> list.add(new ReplyResourceDto(reply)));
        List<ReplyResourceDto> collect = replies.stream().map(ReplyResourceDto::new).collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        return ReplyCollectResources.of(collect, links);
    }

    public static CollectionModel of(BlogDomain blog, List<BlogReplyDomain> replies, UserDomain authUser, String profile) {
//        List<ReplyResourceDto> list = new ArrayList<>();
//        replies.forEach(reply -> list.add(new ReplyResourceDto(blog, reply, authUser)));
        List<ReplyResourceDto> collect = replies.stream().map(reply -> new ReplyResourceDto(blog, reply, authUser)).collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        return ReplyCollectResources.of(collect, links);
    }

    private static List<Link> getSelfLink(BlogDomain blog, ReplyResourceDto dto) {
        WebMvcLinkBuilder linker = WebMvcLinkBuilder.linkTo(BlogReplyApiController.class, blog.getIdx());
        List<Link> links = new ArrayList<>();
        links.add(linker.slash(dto.getIdx()).withSelfRel().withType(HttpMethod.GET.name()));
        return links;
    }

}
