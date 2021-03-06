package kr.co.won.study.domain;

import kr.co.won.user.domain.UserDomain;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

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
    @Column(nullable = false)
    private String name;

    // study path
    @Column(nullable = false, unique = true)
    private String path;

    @Column
    private String shortDescription;

    @Lob
    private String description;

    @Builder.Default
    private boolean deleted = false;

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
    private LocalDateTime recruitingEndDateTime;

    /**
     * TODO check UserDomain or User Email
     * study organizer email
     */
    /*
    @ManyToOne
    @JoinColumn(name = "user_idx",nullable = false)
    private UserDomain organizer;
     */
    @Column(nullable = false)
    private String organizer;
    /**
     * TODO check UserDomain or User Email
     * now just one manager
     * study manager email
     */
    @Column
    private String manager;

    /**
     * 0 is not limit
     */
    // max member number
    @Builder.Default
    private int allowMemberNumber = 0;

    // join member count
    @Builder.Default
    @Column(nullable = false)
    private int memberCount = 0;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    //
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "study", orphanRemoval = true)
    private List<StudyMemberDomain> joinMember = new ArrayList<>();

    /**
     * is Owner only delete study
     */
    public boolean isOrganizer(String userEmail) {
        return this.organizer.equals(userEmail);
    }

    public boolean isOrganizer(UserDomain user) {
        return this.organizer.equals(user.getEmail());
    }

    /**
     * study domain function
     */
    public boolean isManager(String userEmail) {
        return isOrganizer(userEmail) ? true : this.manager.equals(userEmail) ? true : false;
    }

    public boolean isManager(UserDomain user) {
        return isOrganizer(user) ? true : this.manager.equals(user.getEmail()) ? true : false;
    }

    /**
     * joined study possible
     */
    public boolean isJoined() {
        /** member count check */
        return leftMember() && studyRecruitingStatus();
    }


    /**
     * join Study
     */
    public boolean joinStudy(StudyMemberDomain studyMember) {
        if (this.isJoined()) {
            this.joinMember.add(studyMember);
            studyMember.setStudy(this);
            this.memberCount += 1;
            return true;
        }
        return false;
    }

    public boolean joinStudy(StudyMemberDomain... studyMembers) {
        Arrays.stream(studyMembers).forEach(member -> {
            if (this.isJoined()) {
                this.joinMember.add(member);
                member.setStudy(this);
                this.memberCount += 1;
            }
            if (this.allowMemberNumber < this.memberCount) {
                return;
            }
        });
        if ((studyMembers.length + this.memberCount) > this.allowMemberNumber) {
            return false;
        }
        return true;
    }

    /**
     * Join Study Member Over not Join Study
     */
    public boolean joinStudyOverFailed(StudyMemberDomain... studyMembers) {
        // study join possible check
        if (this.allowMemberNumber < (studyMembers.length + this.memberCount)) {
            return false;
        }
        Arrays.stream(studyMembers).forEach(member -> {
            if (this.isJoined()) {
                this.joinMember.add(member);
                member.setStudy(this);
                this.memberCount += 1;
            }
        });
        return true;
    }

    /**
     * possible join left member
     */
    private boolean leftMember() {
        return this.allowMemberNumber == 0 ? true : this.allowMemberNumber > this.memberCount ? true : false;
    }

    /**
     * study status
     */
    private boolean studyRecruitingStatus() {
        return this.closed == false ? this.recruiting == true ? true : false : false;
    }

    /**
     * study publishing
     */
    public void studyPublishing() {
        this.closed = false;
        this.recruiting = false;
        this.published = true;
        this.publishedDateTime = LocalDateTime.now();
    }

    /**
     * Study Recruiting
     */
    public void studyRecruiting(LocalDateTime endTime) {
        if (endTime.equals(LocalDateTime.now())) {
            throw new IllegalArgumentException("can not now time.");
        }
        this.closed = false;
        this.published = false;
        this.recruiting = true;
        this.recruitingEndDateTime = endTime;
    }

    /**
     * Study Closing
     */
    public void studyClosing() {
        this.published = false;
        this.recruiting = false;
        this.closed = true;
        this.closedDateTime = LocalDateTime.now();
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
        /** update time is difference few seconds so add 1 min and compare */
        return this.createdAt.plusMinutes(1).isAfter(updatedAt) ? StudyStatusType.NEW : StudyStatusType.FINISHED;
    }

    /**
     * Join user check function
     */
    public boolean isJoinMember(StudyMemberDomain member) {
        return joinMember.stream().anyMatch(studyMember -> studyMember.equals(member));
    }

    public boolean isJoinMember(UserDomain user) {
        return joinMember.stream().anyMatch(studyMember -> studyMember.getUser().equals(user));
    }

    public boolean isJoinMember(String userEmail) {
        return joinMember.stream().anyMatch(studyMember -> studyMember.getUser().getEmail().equals(userEmail));
    }

}
