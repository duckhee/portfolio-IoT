package kr.co.won.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDto {

    private Long idx;

    private String replyContent;

    private String replyer;

    private String replyerEmail;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
