package kr.co.won.study.api.dto;

import jdk.jfr.DataAmount;
import kr.co.won.study.domain.StudyStatusType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.inject.BindingAnnotation;

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





    private LocalDateTime getStatusTime(LocalDateTime closeDateTime, LocalDateTime publishedDateTime, LocalDateTime recruitingEndDateTime, LocalDateTime createdAt, LocalDateTime updatedAt, StudyStatusType statusType) {
        if (statusType != null) {
            switch (statusType) {
                case CLOSE:
                    return closeDateTime;
                case PUBLISHED:
                    return publishedDateTime;
                case RECRUIT:
                    return recruitingEndDateTime;
                case NEW:
                    return createdAt;
                case FINISHED:
                    return updatedAt;
            }
        }
        return null;
    }


    public StudyStatusType studyStatus(boolean closed, boolean published, boolean recruiting) {
        if (closed == true) {
            return StudyStatusType.CLOSE;
        }
        if (published == true) {
            return StudyStatusType.PUBLISHED;
        }
        if (recruiting == true) {
            return StudyStatusType.RECRUIT;
        }
        /** update time same create time is new else finished */
        return this.createdAt.equals(updatedAt) ? StudyStatusType.NEW : StudyStatusType.FINISHED;
    }
}
