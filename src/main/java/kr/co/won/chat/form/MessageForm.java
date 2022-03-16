package kr.co.won.chat.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageForm {

    private String roomId;

    private String writer;

    private String message;

    private LocalDateTime sendTime;
}
