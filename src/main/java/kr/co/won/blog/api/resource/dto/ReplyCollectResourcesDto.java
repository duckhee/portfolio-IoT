package kr.co.won.blog.api.resource.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import kr.co.won.blog.domain.BlogReplyDomain;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.CollectionModel;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ReplyCollectResourcesDto extends CollectionModel<ReplyCollectResourcesDto> {

    @JsonUnwrapped
    private List<ReplyResourceDto> replies;

    public ReplyCollectResourcesDto(List<BlogReplyDomain> replies) {
        List<ReplyResourceDto> list = new ArrayList<>();
        replies.forEach(reply -> list.add(new ReplyResourceDto(reply)));
        this.replies = list;
    }
}
