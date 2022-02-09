package kr.co.won.user.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * User setting
 * user profile image setting
 * web, email notification
 */
@Getter
@Setter
@Builder
@ToString(exclude = {"user"})
@EqualsAndHashCode(of = {"idx"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_user_settings")
public class UserSettingDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_idx", nullable = false)
    private UserDomain user;

    @Builder.Default
    private boolean emailNotification = false;

    @Builder.Default
    private boolean webNotification = false;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /** user setting domain function */
}
