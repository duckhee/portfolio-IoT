package kr.co.won.chat.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_chatting_msg")
@Getter
@Setter
@Builder
@ToString(exclude = {})
@EqualsAndHashCode(of = {"idx"})
@NoArgsConstructor
@AllArgsConstructor
public class ChattingMessageDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "room_idx", nullable = false)
    private ChattingRoomDomain room;

    @Column(nullable = false)
    private String speaker;

    private String message;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
