package kr.co.won.study.form;

import kr.co.won.study.domain.StudyStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudyForm {

    private String name;
    
    private String path;

    @Min(value = 0, message = "wrong number check.")
    private Integer allowMemberNumber;

    @Length(max = 255, message = "max length is 255.")
    private String shortDescription;

    private String description;

    @NotNull(message = "study status setting required.")
    private StudyStatusType status;

}
