package kr.co.won.study.domain;

import kr.co.won.user.domain.UserDomain;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = {"idx"})
@ToString(exclude = {"joinMember"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_studies")
public class StudyDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    // study name
    private String name;

    // study path
    @Column(nullable = false, unique = true)
    private String path;

    @Column(length = 255)
    private String shortDescription;

    @Lob
    private String description;

    /**
     * Study Start
     */
    @Builder.Default
    private boolean published = false;
    // published time save
    private LocalDateTime publishedDateTime;

    // study close or finished
    @Builder.Default
    private boolean closed = false;
    // published close time
    private LocalDateTime closedDateTime;

    @Builder.Default
    private boolean recruiting = false;
    // recruited start time
    private LocalDateTime recruitingUpdateDateTime;

    /**
     * TODO check UserDomain or User Email
     * study organizer email
     */
    @Column(nullable = false)
    private String organizer;
    /**
     * TODO check UserDomain or User Email
     * study manager email
     */
    @Column(nullable = false)
    private String manager;

    /**
     * 0 is not limit
     */
    // max member number
    @Builder.Default
    private int arrowMemberNumber = 0;

    // join member count
    @Column(nullable = false)
    private int memberCount = 0;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    //
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "study", orphanRemoval = true)
    private List<StudyMemberDomain> joinMember = new ArrayList<>();

    /**
     * is Owner only delete study
     */
    public boolean isOrganizer(String userEmail) {
        return this.organizer.equals(userEmail);
    }

    /**
     * study domain function
     */
    public boolean isManager(String userEmail) {
        return isOrganizer(userEmail) ? true : this.manager.equals(userEmail) ? true : false;
    }

    /**
     * joined study possible
     */
    public boolean isJoined() {
        /** member count check */
        return leftMember() && studyRecruitingStatus();
    }

    /**
     * possible join left member
     */
    private boolean leftMember() {
        return arrowMemberNumber == 0 ? true : arrowMemberNumber > memberCount ? true : false;
    }

    /**
     * study status
     */
    private boolean studyRecruitingStatus() {
        return closed == false ? recruiting == true ? true : false : false;
    }

    public StudyStatusType studyStatus() {
        if (this.closed == true) {
            return StudyStatusType.CLOSE;
        }
        if (this.published == true) {
            return StudyStatusType.PUBLISHED;
        }
        if (this.recruiting == true) {
            return StudyStatusType.RECRUIT;
        }
        /** update time same create time is new else finished */
        return this.createdAt.equals(updatedAt) ? StudyStatusType.NEW : StudyStatusType.FINISHED;
    }

    /**
     * Join user check function
     */
    public boolean isJoinMember(UserDomain user) {
        return joinMember.stream().anyMatch(studyMember -> studyMember.getUser().equals(user));
    }

    public boolean isJoinMember(String userEmail) {
        return joinMember.stream().anyMatch(studyMember -> studyMember.getUser().getEmail().equals(userEmail));
    }
}
