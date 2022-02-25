package kr.co.won.study.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudyForm {

    @NotBlank
    private String name;

    @NotBlank
    private String path;

    @Max(value = 255, message = "max length is 255.")
    private String shortDescription;

    private String description;

    private Integer allowMemberNumber;
}
