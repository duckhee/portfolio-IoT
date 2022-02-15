package kr.co.won.user.api.resource.dto;

import kr.co.won.common.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "users")
public class UserCreateResourceDto {

    private Long idx;

    private String email;

    private String name;

    private Address address;

    private boolean active;

    private String job;

    private boolean emailVerified;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
