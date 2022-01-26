package kr.co.won.user.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserForm {

    @Email(message = "required email")
    private String email;

    @NotNull(message = "required name")
    private String name;

    @Pattern(regexp = "^[0-9|a-z|A-Z|!|@|#|$|%|\\^|&]+$", message = "password pattern 0~9 a~z A~Z !@#$%^&")
    @NotNull
    private String password;

    @NotNull
    @Pattern(regexp = "^[0-9|a-z|A-Z|!|@|#|$|%|\\^|&]+$", message = "not match password")
    private String confirmPassword;

    @NotNull(message = "required")
    private String zipCode;

    @NotNull(message = "required")
    private String roadAddress;

    @NotNull(message = "required")
    private String detailAddress;

}
