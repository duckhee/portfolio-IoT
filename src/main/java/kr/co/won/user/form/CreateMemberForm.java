package kr.co.won.user.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemberForm {

    @Email(message = "required email")
    private String email;

    @NotBlank(message = "required name")
    private String name;
}
