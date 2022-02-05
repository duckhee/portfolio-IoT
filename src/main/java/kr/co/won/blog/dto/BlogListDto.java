package kr.co.won.blog.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogListDto {

    private Long idx;

    private String title;

    private String writer;

    private Integer viewCount;

    private Integer repliesNumber;

    private LocalDateTime createdAt;

    @QueryProjection
    public BlogListDto(Long idx, String title, String writer, Integer viewCount, Integer repliesNumber, LocalDateTime createdAt) {
        this.idx = idx;
        this.title = title;
        this.writer = writer;
        this.viewCount = viewCount;
        this.repliesNumber = repliesNumber;
        this.createdAt = createdAt;
    }
}
