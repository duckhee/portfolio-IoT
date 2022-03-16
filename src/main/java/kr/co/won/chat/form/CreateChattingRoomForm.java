package kr.co.won.chat.form;

import kr.co.won.chat.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateChattingRoomForm {

    @NotEmpty(message = "chatting room is not null.")
    private String name;

    @Builder.Default
    @NotNull(message = "chatting room type select.")
    private RoomType type = RoomType.PUBLIC;

    private String password;

}
