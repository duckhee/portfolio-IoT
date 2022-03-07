package kr.co.won.study.api.resource.dto;

import kr.co.won.study.api.StudyApiController;
import kr.co.won.study.domain.StudyDomain;
import kr.co.won.user.domain.UserDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class StudyListCollectResources extends CollectionModel<StudyListResourceDto> {

    private List<StudyListResourceDto> studies;

    public StudyListCollectResources(List<StudyDomain> study) {
        List<StudyListResourceDto> collectStudy = study.stream().map(StudyListResourceDto::new).collect(Collectors.toList());
        this.studies = collectStudy;
    }

    public StudyListCollectResources(List<StudyDomain> study, UserDomain autUser) {
        List<StudyListResourceDto> collectStudy = study.stream().map(studyDomain -> new StudyListResourceDto(studyDomain, autUser)).collect(Collectors.toList());
        this.studies = collectStudy;
    }

    public static CollectionModel of(List<StudyDomain> studies, UserDomain authUser) {
        List<StudyListResourceDto> collectStudy = studies.stream().map(study -> new StudyListResourceDto(study, authUser)).collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        return StudyListCollectResources.of(collectStudy, links);
    }


    public static CollectionModel of(List<StudyDomain> studies, UserDomain authUser, String profile) {
        List<StudyListResourceDto> collectStudy = studies.stream().map(study -> new StudyListResourceDto(study, authUser)).collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        return StudyListCollectResources.of(collectStudy, links);
    }

    private static List<Link> getSelfLink(StudyDomain study) {
        WebMvcLinkBuilder linker = WebMvcLinkBuilder.linkTo(StudyApiController.class);
        List<Link> links = new ArrayList<>();
        return links;
    }

}
