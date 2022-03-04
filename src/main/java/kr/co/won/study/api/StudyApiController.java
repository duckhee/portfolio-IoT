package kr.co.won.study.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.errors.resource.ValidErrorResource;
import kr.co.won.study.api.assembler.PageStudyAssembler;
import kr.co.won.study.api.resource.StudyCreateResource;
import kr.co.won.study.api.resource.dto.StudyCreateResourceDto;
import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.form.CreateStudyForm;
import kr.co.won.study.form.DeleteBulkForm;
import kr.co.won.study.service.StudyService;
import kr.co.won.study.validation.CreateStudyValidation;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@RestController
@RequestMapping(path = "/api/studies")
@RequiredArgsConstructor
public class StudyApiController {

    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    /**
     * study service
     */
    private final StudyService studyService;

    /**
     * Paging
     */
    private final PageStudyAssembler pageStudyAssembler;
    private final PagedResourcesAssembler pagedResourcesAssembler;

    /**
     * study validation
     */
    private final CreateStudyValidation createStudyValidation;

    @GetMapping
    public ResponseEntity listStudyResources(@AuthUser UserDomain loginUser, PageDto pageDto) {

        Page page = studyService.pagingStudy(pageDto, loginUser);

        return null;
    }

    @PostMapping
    public ResponseEntity createStudyResource(@AuthUser UserDomain authUser, @Validated @RequestBody CreateStudyForm form, Errors errors) {
        if (authUser == null) {
            throw new AccessDeniedException("Not Login.");
        }
        // validation failed
        if (errors.hasErrors()) {
            return validationResources(errors);
        }
        // study custom validation valid
        createStudyValidation.validate(form, errors);
        if (errors.hasErrors()) {
            return validationResources(errors);
        }
        // save study
        StudyDomain mappedStudy = modelMapper.map(form, StudyDomain.class);
        StudyDomain savedStudy = studyService.createStudy(mappedStudy, authUser);
        WebMvcLinkBuilder baseLink = linkTo(StudyApiController.class);
        // creat uri
        URI createUri = URI.create(baseLink.slash(savedStudy.getPath()).toString());
        // return result setting
        StudyCreateResourceDto studyDto = new StudyCreateResourceDto(savedStudy, authUser);
        EntityModel<StudyCreateResourceDto> studyResource = StudyCreateResource.of(studyDto);
        studyResource.add(baseLink.slash(savedStudy.getName()).withRel("query-study").withType(HttpMethod.GET.name()));
        studyResource.add(baseLink.slash(savedStudy.getName()).withRel("list-study").withType(HttpMethod.GET.name()));
        studyResource.add(baseLink.slash(savedStudy.getName()).withRel("update-study").withType(HttpMethod.PUT.name()));
        studyResource.add(baseLink.slash(savedStudy.getName()).withRel("delete-study").withType(HttpMethod.DELETE.name()));
        studyResource.add(Link.of("/docs/index.html#study-create-resources", "profile").withType(HttpMethod.GET.name()));
        return ResponseEntity.created(createUri).body(studyResource);
    }

    /**
     * study find
     */
    @GetMapping(path = "/{studyPath}")
    public ResponseEntity findStudyUsingPathResource(@AuthUser UserDomain loginUser, @PathVariable(name = "studyPath") String path) {
        StudyDomain findStudy = studyService.findStudyWithPath(path, loginUser);
        return null;
    }

    @PutMapping(path = "/{studyIdx}")
    public ResponseEntity updateStudyResource(@PathVariable(name = "studyIdx") Long studyIdx) {
        return null;
    }

    /**
     * study slice update
     */
    @PatchMapping(path = "/{studyIdx}")
    public ResponseEntity updateStudyPartsResource(@PathVariable(name = "studyIdx") Long studyIdx, @AuthUser UserDomain loginUser) {
        return null;
    }

    /**
     * this is using admin user role
     */
    @DeleteMapping
    public ResponseEntity deleteStudyBulkResource(@RequestBody DeleteBulkForm studyIdxes, @AuthUser UserDomain loginUser) {
        log.info("bulk delete study ::: {}", studyIdxes.getStudy());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{studyIdx}")
    public ResponseEntity deleteStudyResource(@PathVariable(name = "studyIdx") Long studyIdx) {
        return null;
    }

    // validation error resource
    private ResponseEntity validationResources(Errors errors) {
        EntityModel<Errors> validationErrorResource = ValidErrorResource.of(errors);
        return ResponseEntity.badRequest().body(validationErrorResource);
    }
}
