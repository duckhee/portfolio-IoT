package kr.co.won.user.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tbl_user_role")
@EqualsAndHashCode(of = {"idx", "role"})
@ToString(exclude = {"user"})
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OnDelete(action = CASCADE)
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
