package kr.co.won.study.api.resource.dto;

import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.domain.StudyStatusType;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

public class CreateStudyResourceDto extends RepresentationModel<CreateStudyResourceDto> {

    private Long idx;

    private String name;

    private String shortDescription;

    private StudyStatusType status;

    private LocalDateTime createdAt;

    public CreateStudyResourceDto(StudyDomain studyDomain) {
        this.idx = studyDomain.getIdx();
        this.name = studyDomain.getName();
        this.shortDescription = studyDomain.getShortDescription();
        this.status = studyDomain.studyStatus();
        this.createdAt = studyDomain.getCreatedAt();
    }

    public CreateStudyResourceDto(StudyDomain studyDomain, UserDomain loginUser) {
        this.idx = studyDomain.getIdx();
        this.name = studyDomain.getName();
        this.shortDescription = studyDomain.getShortDescription();
        this.status = studyDomain.studyStatus();
        this.createdAt = studyDomain.getCreatedAt();
    }

    private boolean isAuth(StudyDomain studyDomain, UserDomain loginUser) {
        return loginUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER) || studyDomain.getOrganizer().equals(loginUser.getEmail());
    }
}
