package kr.co.won.user.api.resource.dto;

import kr.co.won.common.Address;
import kr.co.won.user.api.UserApiController;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.type.UserRoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter

@NoArgsConstructor
@Relation(collectionRelation = "users")
public class UserResourceDto extends RepresentationModel<UserResourceDto> {

    private Long idx;

    private String email;

    private String name;

    private boolean delete;

    private boolean active;

    private Address address;

    private boolean emailVerified;

    private Set<UserRoleType> roles;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public UserResourceDto(UserDomain user){
        this.idx = user.getIdx();
        this.email = user.getEmail();
        this.name = user.getName();
        this.delete = user.isDeleteFlag();
        this.active = user.isActiveFlag();
        this.address = user.getAddress();
        this.emailVerified = user.isEmailVerified();
        this.roles = user.getRoles().stream().map(UserRoleDomain::getRole).collect(Collectors.toSet());
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();

        this.add(WebMvcLinkBuilder.linkTo(UserApiController.class).slash(this.idx).withSelfRel().withType(HttpMethod.GET.name()));
    }

}
