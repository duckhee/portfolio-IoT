package kr.co.won.iot.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSiteForm {

    // iot site
    @NotBlank
    private String siteName;

    // short description
    private String shortDescription;


}
