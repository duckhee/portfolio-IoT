package kr.co.won.user.mapper.vo;

import kr.co.won.user.domain.type.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleVo {

    private Long idx;

    @Builder.Default
    private UserRoleType role = UserRoleType.USER;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
