package kr.co.won.blog.api.resource.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "replies")
public class ReplyResourceDto {

    private Long idx;

    private String replyer;

    private String replyContent;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
