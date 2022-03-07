package kr.co.won.study.api.resource.dto;

import kr.co.won.study.api.StudyApiController;
import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.domain.StudyStatusType;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "studies")
public class StudyListResourceDto extends RepresentationModel<StudyListResourceDto> {

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

    public StudyListResourceDto(StudyDomain study) {
        this.idx = study.getIdx();
        this.name = study.getName();
        this.shortDescription = study.getShortDescription();
        this.organizer = study.getOrganizer();
        this.path = study.getPath();
        this.allowMemberNumber = study.getAllowMemberNumber();
        this.joinMemberNumber = study.getMemberCount();
        this.status = study.studyStatus();
        this.createdAt = study.getCreatedAt();
        this.updatedAt = study.getUpdatedAt();
        // setting status time
        settingStatusTime(study);
        // setting self link
        this.add(WebMvcLinkBuilder.linkTo(StudyApiController.class).slash(study.getIdx()).withSelfRel().withType(HttpMethod.GET.name()));
    }

    public StudyListResourceDto(StudyDomain study, UserDomain loginUser) {
        this.idx = study.getIdx();
        this.name = study.getName();
        this.shortDescription = study.getShortDescription();
        this.organizer = study.getOrganizer();
        this.path = study.getPath();
        this.allowMemberNumber = study.getAllowMemberNumber();
        this.joinMemberNumber = study.getMemberCount();
        this.status = study.studyStatus();
        this.createdAt = study.getCreatedAt();
        this.updatedAt = study.getUpdatedAt();
        // setting status time
        settingStatusTime(study);
        this.add(WebMvcLinkBuilder.linkTo(StudyApiController.class).slash(study.getIdx()).withSelfRel().withType(HttpMethod.GET.name()));
        if (isAuth(loginUser, study)) {
            this.add(WebMvcLinkBuilder.linkTo(StudyApiController.class).slash(study.getIdx()).withRel("update-study").withType(HttpMethod.PUT.name()));
            this.add(WebMvcLinkBuilder.linkTo(StudyApiController.class).slash(study.getIdx()).withRel("delete-study").withType(HttpMethod.DELETE.name()));
        }
    }

    private void settingStatusTime(StudyDomain study) {
        if (study.studyStatus() != null) {
            switch (study.studyStatus()) {
                case NEW:
                    this.statusTime = study.getCreatedAt();
                    break;
                case CLOSE:
                    this.statusTime = study.getClosedDateTime();
                    break;
                case PUBLISHED:
                    this.statusTime = study.getPublishedDateTime();
                    break;
                case RECRUIT:
                    this.statusTime = study.getRecruitingUpdateDateTime();
                    break;
                case FINISHED:
                    this.statusTime = study.getUpdatedAt();
                    break;
            }
        }
    }

    private boolean isAuth(UserDomain loginUser, StudyDomain study) {
        return loginUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER) || study.getOrganizer().equals(loginUser.getEmail()) || study.getManager().equals(loginUser.getEmail());
    }
}
