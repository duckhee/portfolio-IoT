package kr.co.won.websocket.stomp.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMsgForm {

    @NotBlank(message = "need to user id.")
    private String userId;

    private String content;

    private String fromUserId;

    @Builder.Default
    private boolean deleted = false;

    @Builder.Default
    private LocalDate createdAt = LocalDate.now();


}
