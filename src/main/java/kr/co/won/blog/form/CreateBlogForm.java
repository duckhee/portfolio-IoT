package kr.co.won.blog.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.net.URI;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBlogForm {

    @NotBlank(message = "required title.")
    private String title;

    private String content;

    private URI projectUri;

}
