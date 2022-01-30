package kr.co.won.user.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.address.Address;
import kr.co.won.auth.AuthUser;
import kr.co.won.errors.resource.ValidErrorResource;
import kr.co.won.user.api.resource.dto.UserResourceDto;
import kr.co.won.user.api.resource.UserResource;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.form.CreateUserForm;
import kr.co.won.user.service.UserService;
import kr.co.won.user.validation.CreateUserValidation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/users")
public class UserApiController {

    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    @Resource(name = "adminUserService")
    private final UserService adminUserService;

    @Resource(name = "userService")
    private final UserService userService;

    private final CreateUserValidation createUserValidation;


    @PostMapping
    public ResponseEntity createUserResource(@Validated @RequestBody CreateUserForm form, Errors errors) {
        // jsr304 validation check
        if (errors.hasErrors()) {
            return validationErrorResponse(errors);
        }
        // validation createUserForm
        createUserValidation.validate(form, errors);
        // validation check
        if (errors.hasErrors()) {
            return validationErrorResponse(errors);
        }
        // form get user information and create domain
        UserDomain mappedUser = modelMapper.map(form, UserDomain.class);
        // form get user Address
        Address userAddress = new Address(form.getZipCode(), form.getRoadAddress(), form.getDetailAddress());
        mappedUser.setAddress(userAddress);

        UserDomain savedUser = userService.createUser(mappedUser);
        // mapping user dto
        UserResourceDto mappedUserResource = modelMapper.map(savedUser, UserResourceDto.class);
        // make resource
        EntityModel<UserResourceDto> resultResource = UserResource.of(mappedUserResource);
        // base hateoas controller link
        WebMvcLinkBuilder baseLink = linkTo(UserApiController.class);
        //resultResource.add(baseLink.withRel("list-users"));
        // add hateoas link
        resultResource.add(baseLink.slash(savedUser.getIdx()).withRel("delete-users"));
        // default links
        resultResource.add(baseLink.slash(savedUser.getIdx()).withRel("query-users"));
        resultResource.add(baseLink.slash(savedUser.getIdx()).withRel("update-users"));
        resultResource.add(Link.of("/docs/index.html#user-create-resources", "profile"));
        // create uri
        URI createUri = baseLink.slash(savedUser.getIdx()).toUri();
        // return result
        return ResponseEntity.created(createUri).body(resultResource);
    }

    @GetMapping(path = "/{idx}")
    public ResponseEntity findUserResource(@AuthUser UserDomain authUser, @PathVariable(value = "idx") Long idx) {
        // TODO Admin User find detail
        UserDomain findUserWithAdmin = adminUserService.findUser(idx, authUser);

        return ResponseEntity.ok().build();
    }

    /**
     * validation failed error resource
     */
    private ResponseEntity validationErrorResponse(Errors errors) {
        // validation error resource
        EntityModel<Errors> errorResource = ValidErrorResource.of(errors);
        errorResource.add(linkTo(UserApiController.class).withRel("create-users"));
//        errorResource.add(linkTo("/docs/index.html#create-users-errors").withRel("profile"));
        return ResponseEntity.badRequest().body(errorResource);
    }

}
