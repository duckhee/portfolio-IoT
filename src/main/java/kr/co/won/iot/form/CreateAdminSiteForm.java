package kr.co.won.iot.form;

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
public class CreateAdminSiteForm {

    // site name
    @NotBlank
    private String siteName;

    // site user email
    @Email
    @NotBlank
    private String userEmail;

    // site short description
    private String shortDescription;
}
