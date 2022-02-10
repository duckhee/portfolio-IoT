package kr.co.won.blog.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.net.URL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogForm {

    @NotBlank(message = "required title.")
    private String title;

    private String content;

    private URL projectUrl;

    private MultipartFile projectFile;

    private String resources;
}
