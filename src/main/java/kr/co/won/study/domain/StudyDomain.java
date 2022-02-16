package kr.co.won.study.domain;

import kr.co.won.user.domain.UserDomain;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = {"idx"})
@ToString//(exclude = {})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_studies")
public class StudyDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String title;

    // study path
    @Column(nullable = false, unique = true)
    private String path;

    @Column(length = 255)
    private String shortDescription;

    @Lob
    private String description;

    // published time save
    private LocalDateTime publishedDateTime;
    // published close time
    private LocalDateTime closedDateTime;

    // recruited start time
    private LocalDateTime recruitingUpdateDateTime;

    @Builder.Default
    private boolean recruiting = false;

    @Builder.Default
    private boolean published = false;

    // study close or finished
    @Builder.Default
    private boolean closed = false;

    /**
     * TODO check UserDomain or User Email
     * study organizer email
     */
    private String organizer;

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

    /** study domain function */

}
