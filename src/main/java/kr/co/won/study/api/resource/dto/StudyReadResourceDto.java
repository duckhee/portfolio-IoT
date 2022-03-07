package kr.co.won.study.api.resource.dto;

import kr.co.won.study.api.StudyApiController;
import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.domain.StudyStatusType;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StudyReadResourceDto extends RepresentationModel<StudyReadResourceDto> {

    private Long idx;
    private String name;
    private String organizer;
    private String manager;
    private String path;
    private Integer allowMemberNumber;
    private Integer joinMemberNumber;
    private String shortDescription;
    private String description;
    private StudyStatusType status;
    private LocalDateTime createdAt;


    public StudyReadResourceDto(StudyDomain study) {
        this.idx = study.getIdx();
        this.name = study.getName();
        this.organizer = study.getOrganizer();
        this.manager = study.getManager();
        this.path = study.getPath();
        this.allowMemberNumber = study.getAllowMemberNumber();
        this.joinMemberNumber = study.getMemberCount();
        this.shortDescription = study.getShortDescription();
        this.description = study.getDescription();
        this.status = study.studyStatus();
        this.createdAt = study.getCreatedAt();
        this.add(WebMvcLinkBuilder.linkTo(StudyApiController.class).slash(this.idx).withSelfRel().withType(HttpMethod.GET.name()));
    }

    public StudyReadResourceDto(StudyDomain study, UserDomain loginUser) {
        this.idx = study.getIdx();
        this.name = study.getName();
        this.organizer = study.getOrganizer();
        this.manager = study.getManager();
        this.path = study.getPath();
        this.allowMemberNumber = study.getAllowMemberNumber();
        this.joinMemberNumber = study.getMemberCount();
        this.shortDescription = study.getShortDescription();
        this.description = study.getDescription();
        this.status = study.studyStatus();
        this.createdAt = study.getCreatedAt();
        this.add(WebMvcLinkBuilder.linkTo(StudyApiController.class).slash(this.idx).withSelfRel().withType(HttpMethod.GET.name()));
        if (isAuth(study, loginUser)) {
            this.add(WebMvcLinkBuilder.linkTo(StudyApiController.class).slash(this.idx).withRel("update-study").withType(HttpMethod.PUT.name()));
            this.add(WebMvcLinkBuilder.linkTo(StudyApiController.class).slash(this.idx).withRel("delete-study").withType(HttpMethod.DELETE.name()));
        }
    }


    // link auth Check
    private boolean isAuth(StudyDomain study, UserDomain authUser) {
        return authUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER) || study.getOrganizer().equals(authUser.getEmail()) || study.getManager().equals(authUser.getEmail());
    }
}
