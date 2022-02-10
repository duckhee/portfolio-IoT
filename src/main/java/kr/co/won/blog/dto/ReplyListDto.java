package kr.co.won.blog.dto;

import kr.co.won.blog.domain.BlogReplyDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyListDto {

    private Long idx;

    private String replyContent;

    private String replyer;

    private String replyerEmail;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public ReplyListDto(BlogReplyDomain reply) {
        this.idx = reply.getIdx();
        this.replyContent = reply.getReplyContent();
        this.replyer = reply.getReplyer();
        this.replyerEmail = reply.getReplyerEmail();
        this.createdAt = reply.getCreatedAt();
        this.updatedAt = reply.getUpdatedAt();
    }
}
