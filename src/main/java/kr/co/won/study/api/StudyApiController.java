package kr.co.won.study.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.errors.resource.ValidErrorResource;
import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.form.CreateStudyForm;
import kr.co.won.study.form.DeleteBulkForm;
import kr.co.won.study.service.StudyService;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    private final StudyService studyService;

    @GetMapping
    public ResponseEntity listStudyResources(PageDto pageDto) {
        return null;
    }

    @PostMapping
    public ResponseEntity createStudyResource(@AuthUser UserDomain authUser, @Validated CreateStudyForm form, Errors errors) {
        if (authUser == null) {
            throw new AccessDeniedException("Not Login.");
        }
        // validation failed
        if (errors.hasErrors()) {
            return validationResources(errors);
        }
        StudyDomain mappedStudy = modelMapper.map(form, StudyDomain.class);
        WebMvcLinkBuilder baseLink = linkTo(StudyApiController.class);
        return null;
    }


    @PutMapping(path = "/{studyIdx}")
    public ResponseEntity updateStudyResource(@PathVariable(name = "studyIdx") Long studyIdx) {
        return null;
    }

    @PatchMapping(path = "/{studyIdx}")
    public ResponseEntity updateStudyPartsResource(@PathVariable(name = "studyIdx") Long studyIdx) {
        return null;
    }

    @DeleteMapping
    public ResponseEntity deleteStudyBulkResource(@RequestBody DeleteBulkForm studyIdxes) {
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
