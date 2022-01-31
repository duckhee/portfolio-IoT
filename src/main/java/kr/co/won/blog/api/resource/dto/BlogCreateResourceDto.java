package kr.co.won.blog.api.resource.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "blogs")
public class BlogCreateResourceDto extends RepresentationModel<BlogCreateResourceDto> {

    private Long idx;

    private String title;

    private String content;

    private String writer;

    private String writerEmail;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    List<ReplyResourceDto> replies;
}
