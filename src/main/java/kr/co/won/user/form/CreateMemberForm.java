package kr.co.won.user.form;

import kr.co.won.user.domain.type.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemberForm {

    @Email(message = "required email")
    private String email;

    @NotBlank(message = "required name")
    private String name;

    private Set<UserRoleType> roles;

    @NotBlank(message = "required")
    private String zipCode;

    @NotBlank(message = "required")
    private String roadAddress;

    @NotBlank(message = "required")
    private String detailAddress;

}
