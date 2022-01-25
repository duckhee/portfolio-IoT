package kr.co.won.user.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tbl_user_role")
@EqualsAndHashCode(of = {"idx"})
@ToString(exclude = {"user"})
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_idx")
    private UserDomain user;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private UserRoleType role = UserRoleType.USER;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
