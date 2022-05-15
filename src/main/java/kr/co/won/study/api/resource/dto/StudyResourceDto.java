package kr.co.won.study.api.resource.dto;

import kr.co.won.study.api.StudyApiController;
import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.domain.StudyStatusType;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.type.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StudyResourceDto extends RepresentationModel<StudyResourceDto> {

    private Long idx;

    private String name;

    private String shortDescription;

    private String path;

    private StudyStatusType status;

    private LocalDateTime createdAt;


    public StudyResourceDto(StudyDomain study) {
        this.idx = study.getIdx();
        this.name = study.getName();
        this.shortDescription = study.getShortDescription();
        this.path = study.getPath();
        this.status = study.studyStatus();
        this.createdAt = study.getCreatedAt();
        this.add(WebMvcLinkBuilder.linkTo(StudyApiController.class).slash(this.idx).withSelfRel().withType(HttpMethod.GET.name()));
    }

    public StudyResourceDto(StudyDomain study, UserDomain loginUser) {
        this.idx = study.getIdx();
        this.name = study.getName();
        this.shortDescription = study.getShortDescription();
        this.path = study.getPath();
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
