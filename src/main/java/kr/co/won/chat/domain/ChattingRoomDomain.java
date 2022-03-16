package kr.co.won.chat.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString(exclude = {"msg"})
@EqualsAndHashCode(of = {"idx", "roomId"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_chatting_rome")
public class ChattingRoomDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Builder.Default
    @Column(nullable = false, unique = true)
    private String roomId = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String password;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    @Column(nullable = false)
    private String ownerEmail;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "room", orphanRemoval = true)
    List<ChattingMessageDomain> msg = new ArrayList<>();

    /** chatting room domain function */

    /**
     * Generate Chatting Room ID
     */
    public void generateRoomId() {
        this.roomId = UUID.randomUUID().toString();
    }

    /**
     * chatting room add msg
     */
    public void addMsg(ChattingMessageDomain msg) {
        this.msg.add(msg);
        msg.setRoom(this);
    }

    public void removeMsg(ChattingMessageDomain msg) {
        this.msg.remove(msg);
        msg.setRoom(null);
    }
}
