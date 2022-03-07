package kr.co.won.study.dto;

import com.querydsl.core.annotations.QueryProjection;
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

    @QueryProjection
    public StudyListQueryDto(Long idx, String name, String shortDescription, String organizer, String path, Integer allowMemberNumber, Integer joinMemberNumber, boolean closed, LocalDateTime closeDateTime, boolean published, LocalDateTime publishedDateTime, boolean recruiting, LocalDateTime recruitingUpdateDateTime, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idx = idx;
        this.name = name;
        this.shortDescription = shortDescription;
        this.organizer = organizer;
        this.path = path;
        this.allowMemberNumber = allowMemberNumber;
        this.joinMemberNumber = joinMemberNumber;

        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        // get study status
        StudyStatusType statusType = studyStatus(closed, published, recruiting);
        this.status = statusType;
        // get status time
        LocalDateTime statusTime = getStatusTime(closeDateTime, publishedDateTime, recruitingUpdateDateTime, createdAt, updatedAt, statusType);
        this.statusTime = statusTime;
    }

    private LocalDateTime getStatusTime(LocalDateTime closeDateTime, LocalDateTime publishedDateTime, LocalDateTime recruitingUpdateDateTime, LocalDateTime createdAt, LocalDateTime updatedAt, StudyStatusType statusType) {
        if (statusType != null) {
            switch (statusType) {
                case CLOSE:
                    return closeDateTime;
                case PUBLISHED:
                    return publishedDateTime;
                case RECRUIT:
                    return recruitingUpdateDateTime;
                case NEW:
                    return createdAt;
                case FINISHED:
                    return updatedAt;
            }
        }
        return null;
    }


    public StudyStatusType studyStatus(boolean closed, boolean published, boolean recruiting) {
        if (closed) {
            return StudyStatusType.CLOSE;
        }
        if (published) {
            return StudyStatusType.PUBLISHED;
        }
        if (recruiting) {
            return StudyStatusType.RECRUIT;
        }
        /** update time same create time is new else finished */
        return this.createdAt.equals(updatedAt) ? StudyStatusType.NEW : StudyStatusType.FINISHED;
    }
}
