package kr.co.won.study.api.resource.dto;

import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.domain.StudyStatusType;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.type.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyCreateResourceDto extends RepresentationModel<StudyCreateResourceDto> {

    private Long idx;

    private String name;

    private String shortDescription;

    private String path;

    private StudyStatusType status;

    private LocalDateTime createdAt;

    public StudyCreateResourceDto(StudyDomain studyDomain) {
        this.idx = studyDomain.getIdx();
        this.name = studyDomain.getName();
        this.path = studyDomain.getPath();
        this.shortDescription = studyDomain.getShortDescription();
        this.status = studyDomain.studyStatus();
        this.createdAt = studyDomain.getCreatedAt();
    }

    public StudyCreateResourceDto(StudyDomain studyDomain, UserDomain loginUser) {
        this.idx = studyDomain.getIdx();
        this.name = studyDomain.getName();
        this.path = studyDomain.getPath();
        this.shortDescription = studyDomain.getShortDescription();
        this.status = studyDomain.studyStatus();
        this.createdAt = studyDomain.getCreatedAt();


    }

    private boolean isAuth(StudyDomain studyDomain, UserDomain loginUser) {
        return loginUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER) || studyDomain.getOrganizer().equals(loginUser.getEmail());
    }
}
