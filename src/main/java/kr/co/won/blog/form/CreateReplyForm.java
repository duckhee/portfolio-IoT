package kr.co.won.blog.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReplyForm {

    @NotNull(message = "blog required.")
    private Long blogIdx;

    @NotNull(message = "reply content required.")
    private String replyContent;
}
