package kr.co.won.iot.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.errors.resource.ValidErrorResource;
import kr.co.won.iot.form.CreateSiteForm;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageAssembler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(path = "/api/iot")
@RequiredArgsConstructor
public class IoTApiController {

    /**
     * Null Value Skip Model Mapper
     */
    @Resource(name = "skipModelMapper")
    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    /** Site And Device validation  */

    /**
     * Paging Assembler
     */
    private final PagedResourcesAssembler pageAssembler;

    /**
     * IoT Device Or Site Paging
     */
    @GetMapping
    public ResponseEntity IoTListResource(@AuthUser UserDomain loginUser) {
        return null;
    }

    /**
     * IoT Device Or Site Create
     */
    @PostMapping
    public ResponseEntity IoTDeviceCreateResource(@AuthUser UserDomain loginUser, @Validated CreateSiteForm form, Errors errors) {
        if (loginUser == null) {
            // spring security accessDenied
            throw new AccessDeniedException("not login user.");
        }
        // validation error
        if (errors.hasErrors()) {
            // validation failed resource
            return validationResources(errors);
        }

        return null;
    }


    private ResponseEntity validationResources(Errors errors) {
        EntityModel<Errors> validationErrorResource = ValidErrorResource.of(errors);
        return ResponseEntity.badRequest().body(validationErrorResource);
    }

}
