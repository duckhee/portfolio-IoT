package kr.co.won.user.api.resource.dto;

import kr.co.won.address.Address;
import kr.co.won.user.domain.UserRoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.Set;

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

}
