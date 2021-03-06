package kr.co.won.study.domain;

import kr.co.won.user.domain.UserDomain;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * study join member save domain
 */

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(of = {"idx"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_join_study_member")
public class StudyMemberDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx", nullable = true)
    private UserDomain user;

    /**
     * manager is true
     */
    private boolean manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_idx")
    private StudyDomain study;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /** study join member domain function */


}
