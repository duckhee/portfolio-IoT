package kr.co.won.user.mapper.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {

    private Long idx;

    private String email;

    private String name;

    private String password;

    @Builder.Default
    private Set<UserRoleVo> role = new HashSet<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
