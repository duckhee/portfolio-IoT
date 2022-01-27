package kr.co.won.user.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserForm {

    @Email(message = "required email")
    private String email;

    @NotBlank(message = "required name")
    private String name;

    @Pattern(regexp = "^[0-9|a-z|A-Z|!|@|#|$|%|\\^|&]+$", message = "password pattern 0~9 a~z A~Z !@#$%^&")
    @NotBlank(message = "required password.")
    private String password;


    @Pattern(regexp = "^[0-9|a-z|A-Z|!|@|#|$|%|\\^|&]+$", message = "not match password")
    @NotBlank(message = "required confirm password.")
    private String confirmPassword;

    @NotBlank(message = "required")
    private String zipCode;

    @NotBlank(message = "required")
    private String roadAddress;

    @NotBlank(message = "required")
    private String detailAddress;

}
