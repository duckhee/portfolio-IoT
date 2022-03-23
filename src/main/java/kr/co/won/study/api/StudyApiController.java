package kr.co.won.study.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.errors.resource.ValidErrorResource;
import kr.co.won.study.api.assembler.PageStudyAssembler;
import kr.co.won.study.api.resource.StudyCreateResource;
import kr.co.won.study.api.resource.dto.StudyCreateResourceDto;
import kr.co.won.study.api.resource.dto.StudyReadResourceDto;
import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.form.CreateStudyForm;
import kr.co.won.study.form.DeleteBulkForm;
import kr.co.won.study.form.UpdateStudyForm;
import kr.co.won.study.service.StudyService;
import kr.co.won.study.validation.CreateStudyValidation;
import kr.co.won.study.validation.UpdateStudyValidation;
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
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.http.dsl.Http;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    private final UpdateStudyValidation updateStudyValidation;

    @GetMapping
    public ResponseEntity listStudyResources(@AuthUser UserDomain loginUser, PageDto pageDto) {

        Page page = studyService.pagingStudy(pageDto, loginUser);
        PagedModel pagedModel = pagedResourcesAssembler.toModel(page, pageStudyAssembler);
        // self link make
        WebMvcLinkBuilder webMvcLinkBuilder = linkTo(methodOn(StudyApiController.class).listStudyResources(loginUser, pageDto));
        pagedModel.add(Link.of("/docs/index.html#study-list-resources", "profile"));
        pagedModel.add(webMvcLinkBuilder.withSelfRel());
        return ResponseEntity.ok().body(pagedModel);
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
//        StudyDomain findStudy = studyService.findStudyWithPath(path, loginUser);
        StudyDomain findStudy = studyService.findStudyWithPath(path);
        StudyReadResourceDto studyReadResourceDto = new StudyReadResourceDto(findStudy, loginUser);
        studyReadResourceDto.add(Link.of("/docs/index.html#study-read-resources", "profile").withType(HttpMethod.GET.name()));
        return ResponseEntity.ok().body(studyReadResourceDto);
    }

    /**
     * Update User ALl
     */
    @PutMapping(path = "/{studyPath}")
    public ResponseEntity updateStudyResource(@PathVariable(name = "studyPath") String studyPath, @AuthUser UserDomain loginUser, @Validated @RequestBody UpdateStudyForm form, Errors errors) {
        if (errors.hasErrors()) {
            return validationResources(errors);
        }

        return null;
    }

    /**
     * study slice update
     */
    @PatchMapping(path = "/{studyPath}")
    public ResponseEntity updateStudyPartsResource(@PathVariable(name = "studyPath") String studyPath, @AuthUser UserDomain loginUser, @RequestBody @Validated UpdateStudyForm form, Errors errors) {
        log.info("get update study api controller");
        // jsr validation
        if (errors.hasErrors()) {
            return validationResources(errors);
        }
        // custom validation
        updateStudyValidation.validate(form, errors);
        if (errors.hasErrors()) {
            return validationResources(errors);
        }
        StudyDomain mappedStudy = modelMapper.map(form, StudyDomain.class);
        mappedStudy.setJoinMember(null);
        StudyDomain updateStudy = studyService.updateStudy(studyPath, mappedStudy, loginUser);
        // TODO Update DO resource check
        StudyReadResourceDto resultResource = new StudyReadResourceDto(updateStudy, loginUser);
        resultResource.add(Link.of("/docs/index.html#study-update-resources", "profile"));
        return ResponseEntity.ok().body(resultResource);
    }

    /**
     * this is using admin user role
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.GONE)
    public ResponseEntity deleteStudyBulkResource(@RequestBody DeleteBulkForm studyIdxes, @AuthUser UserDomain loginUser) {
        log.info("bulk delete study ::: {}", studyIdxes.getStudy());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{studyIdx}")
    @ResponseStatus(HttpStatus.GONE)
    public ResponseEntity deleteStudyResource(@AuthUser UserDomain loginUser, @PathVariable(name = "studyIdx") Long studyIdx) {
        StudyDomain deleteStudy = studyService.deleteStudy(studyIdx, loginUser);
        RepresentationModel emptyResource = new RepresentationModel();
        WebMvcLinkBuilder baseLink = linkTo(StudyApiController.class);
        // list study link add
        emptyResource.add(baseLink.withRel("list-study").withType(HttpMethod.GET.name()));
        // create study link add
        emptyResource.add(baseLink.withRel("create-study").withType(HttpMethod.POST.name()));
        // self links
        emptyResource.add(baseLink.slash(deleteStudy.getIdx()).withSelfRel().withType(HttpMethod.GET.name()));
        // profile link add
        emptyResource.add(Link.of("/docs/index.html#study-delete-resources", "profile").withType(HttpMethod.GET.name()));

        if (deleteStudy.isDeleted()) {
            return ResponseEntity.status(HttpStatus.GONE).body(emptyResource);
        }
        return ResponseEntity.badRequest().build();
    }

    // validation error resource
    private ResponseEntity validationResources(Errors errors) {
        log.info("get validation error ::: {}", errors);
        EntityModel<Errors> validationErrorResource = ValidErrorResource.of(errors);
        return ResponseEntity.badRequest().body(validationErrorResource);
    }
}
