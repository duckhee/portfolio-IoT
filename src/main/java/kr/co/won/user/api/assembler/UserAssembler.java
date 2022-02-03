package kr.co.won.user.api.assembler;

import kr.co.won.user.api.UserApiController;
import kr.co.won.user.api.resource.dto.UserResourceDto;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserAssembler implements RepresentationModelAssembler<UserDomain, UserResourceDto> {

    private final ModelMapper modelMapper;

    @Override
    public UserResourceDto toModel(UserDomain entity) {
        UserResourceDto mappedUser = modelMapper.map(entity, UserResourceDto.class);

        Set<UserRoleType> roles = entity.getRoles().stream().map(UserRoleDomain::getRole).collect(Collectors.toSet());
        mappedUser.setAddress(entity.getAddress());
        mappedUser.setActive(entity.isActiveFlag());
        mappedUser.setDelete(entity.isDeleteFlag());
        mappedUser.setRoles(roles);
        WebMvcLinkBuilder selfLink = WebMvcLinkBuilder.linkTo(UserApiController.class);
        mappedUser.add(selfLink.slash(entity.getIdx()).withSelfRel());
//        mappedUser.add(selfLink.slash(entity.getIdx()).withRel("query-users").withType(HttpMethod.GET.name()));
        mappedUser.add(selfLink.slash(entity.getIdx()).withRel("update-users").withType(HttpMethod.PUT.name()));
        mappedUser.add(selfLink.slash(entity.getIdx()).withRel("delete-users").withType(HttpMethod.DELETE.name()));
        return mappedUser;
    }

    public UserResourceDto toModel(UserDomain entity, UserDomain authUser) {
        UserResourceDto mappedUser = modelMapper.map(entity, UserResourceDto.class);
        Set<UserRoleType> roles = entity.getRoles().stream().map(UserRoleDomain::getRole).collect(Collectors.toSet());
        mappedUser.setAddress(entity.getAddress());
        mappedUser.setActive(entity.isActiveFlag());
        mappedUser.setDelete(entity.isDeleteFlag());
        mappedUser.setRoles(roles);
        WebMvcLinkBuilder selfLink = WebMvcLinkBuilder.linkTo(UserApiController.class);
        mappedUser.add(selfLink.slash(entity.getIdx()).withSelfRel().withType(HttpMethod.GET.name()));
        if (authUser.hasRole(UserRoleType.ADMIN)) {
            mappedUser.add(selfLink.slash(entity.getIdx()).withRel("update-users").withType(HttpMethod.PUT.name()));
            mappedUser.add(selfLink.slash(entity.getIdx()).withRel("delete-users").withType(HttpMethod.DELETE.name()));
        }
        return mappedUser;
    }

    @Override
    public CollectionModel<UserResourceDto> toCollectionModel(Iterable<? extends UserDomain> entities) {
        List<UserResourceDto> list = new ArrayList<>();
        entities.forEach(entity -> list.add(this.toModel(entity)));
        return CollectionModel.of(list);
    }

    public CollectionModel<UserResourceDto> toAuthCollection(Iterable<? extends UserDomain> entities, UserDomain authUser) {
        List<UserResourceDto> list = new ArrayList<>();
        entities.forEach(entity -> list.add(this.toModel(entity, authUser)));
        return CollectionModel.of(list);

    }
}
