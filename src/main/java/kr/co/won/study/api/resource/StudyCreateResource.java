package kr.co.won.study.api.resource;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import kr.co.won.study.api.StudyApiController;
import kr.co.won.study.api.resource.dto.StudyCreateResourceDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;


public class StudyCreateResource extends EntityModel<StudyCreateResource> {

    @JsonUnwrapped
    private StudyCreateResourceDto dto;

    StudyCreateResource(StudyCreateResourceDto dto) {
        this.dto = dto;
    }

    public StudyCreateResourceDto getDto() {
        return this.dto;
    }

    private static WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(StudyApiController.class);

    public static EntityModel<StudyCreateResourceDto> of(StudyCreateResourceDto study) {
        List<Link> links = getSelfLink(study);
        return StudyCreateResource.of(study, links);
    }

    public static EntityModel<StudyCreateResourceDto> of(StudyCreateResourceDto study, String profile) {
        List<Link> links = getSelfLink(study);
        links.add(Link.of(profile, "profile").withType(HttpMethod.GET.name()));
        return StudyCreateResource.of(study, links);
    }

    private static List<Link> getSelfLink(StudyCreateResourceDto study) {
        List<Link> links = new ArrayList<>();
        links.add(linkBuilder.slash(study.getIdx()).withSelfRel().withType(HttpMethod.GET.name()));
        return links;
    }


}
