package kr.co.won.chat.domain.redis;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"roomId", "idx"})
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "chatRoom")
public class ChatRoomDomain {


    @Id
    private String roomId;

    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
