package kr.co.won.user.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.common.Address;
import kr.co.won.auth.AuthUser;
import kr.co.won.errors.resource.ValidErrorResource;
import kr.co.won.user.api.assembler.UserAssembler;
import kr.co.won.user.api.resource.dto.UserCreateResourceDto;
import kr.co.won.user.api.resource.UserCreateResource;
import kr.co.won.user.api.resource.dto.UserResourceDto;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.type.UserRoleType;
import kr.co.won.user.form.CreateMemberForm;
import kr.co.won.user.form.CreateUserForm;
import kr.co.won.user.service.UserService;
import kr.co.won.user.validation.CreateMemberValidation;
import kr.co.won.user.validation.CreateUserValidation;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
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
    private final CreateMemberValidation memberValidation;

    /**
     * paging assembler
     */
    private final PagedResourcesAssembler pagedResourcesAssembler;
    private final UserAssembler userAssembler;

    @GetMapping
    public ResponseEntity listUserResource(@AuthUser UserDomain loginUser, PageDto page) {
        log.info("get loginUser ::: {}", loginUser);
        Page pagingResult = adminUserService.pagingUser(page);
        PagedModel resultResource = pagedResourcesAssembler.toModel(pagingResult, userAssembler);

        // webLink Base
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(UserApiController.class);
        resultResource.add(Link.of("/docs/index.html#user-list-resources", "profile").withType(HttpMethod.GET.name()));
        return ResponseEntity.ok(resultResource);
    }

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
        UserCreateResourceDto mappedUserResource = modelMapper.map(savedUser, UserCreateResourceDto.class);
        mappedUserResource.setActive(savedUser.isActiveFlag());
        // make resource
        EntityModel<UserCreateResourceDto> resultResource = UserCreateResource.of(mappedUserResource);
        // base hateoas controller link
        WebMvcLinkBuilder baseLink = linkTo(UserApiController.class);
        // TODO Admin User Show List Users
        //resultResource.add(baseLink.withRel("list-users"));
        // add hateoas link
        resultResource.add(baseLink.slash(savedUser.getIdx()).withRel("delete-users").withType(HttpMethod.DELETE.name()));
        // default links
        resultResource.add(baseLink.slash(savedUser.getIdx()).withRel("query-users").withType(HttpMethod.GET.name()));
        resultResource.add(baseLink.slash(savedUser.getIdx()).withRel("update-users").withType(HttpMethod.PUT.name()));
        resultResource.add(Link.of("/docs/index.html#user-create-resources", "profile").withType(HttpMethod.GET.name()));
        // create uri
        URI createUri = baseLink.slash(savedUser.getIdx()).toUri();
        // return result
        return ResponseEntity.created(createUri).body(resultResource);
    }

    @PostMapping(path = "/auth")
    public ResponseEntity createMemberResource(@Validated @RequestBody CreateMemberForm form, Errors errors) {
        return null;
    }

    @GetMapping(path = "/{idx}")
    public ResponseEntity findUserResource(@AuthUser UserDomain authUser, @PathVariable(value = "idx") Long idx) {
        // TODO Admin User find detail
        UserDomain findUserWithAdmin = adminUserService.findUser(idx, authUser);
//        UserResourceDto userResources = userAssembler.toModel(findUserWithAdmin);
        // UserResourceDto convertor Input UserDomain
        UserResourceDto userResources = new UserResourceDto(findUserWithAdmin);

        // base link builder
        WebMvcLinkBuilder baseLinkBuilder = WebMvcLinkBuilder.linkTo(UserApiController.class);
        if (authUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER)) {
            userResources.add(baseLinkBuilder.slash(findUserWithAdmin.getIdx()).withRel("delete-users").withType(HttpMethod.DELETE.name()));
            userResources.add(baseLinkBuilder.slash(findUserWithAdmin.getIdx()).withRel("update-users").withType(HttpMethod.PUT.name()));
        }
        // profile link add
        userResources.add(Link.of("/docs/index.html#user-query-resources").withRel("profile").withType(HttpMethod.GET.name()));
        return ResponseEntity.ok().body(userResources);
    }

    /**
     * validation failed error resource
     */
    private ResponseEntity validationErrorResponse(Errors errors) {
        // validation error resource
        EntityModel<Errors> errorResource = ValidErrorResource.of(errors);
        errorResource.add(linkTo(UserApiController.class).withRel("create-users").withType(HttpMethod.POST.name()));
//        errorResource.add(linkTo("/docs/index.html#create-users-errors").withRel("profile"));
        return ResponseEntity.badRequest().body(errorResource);
    }

}
