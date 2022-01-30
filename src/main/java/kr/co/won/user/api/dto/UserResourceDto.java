package kr.co.won.user.api.dto;

import kr.co.won.address.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResourceDto {

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
