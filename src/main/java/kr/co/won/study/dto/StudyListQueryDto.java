package kr.co.won.study.dto;

import kr.co.won.study.domain.StudyStatusType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class StudyListQueryDto {


    private Long idx;

    private String name;

    private String shortDescription;

    private String organizer;

    private String path;

    private Integer allowMemberNumber;

    private Integer joinMemberNumber;

    private StudyStatusType status;

    private LocalDateTime statusTime;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
