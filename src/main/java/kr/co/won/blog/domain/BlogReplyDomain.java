package kr.co.won.blog.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_blog_reply")
@Getter
@Setter
@EqualsAndHashCode(of = {"idx"})
@ToString(exclude = {"blog"})
@NoArgsConstructor
@AllArgsConstructor
public class BlogReplyDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    private BlogDomain blog;

    @Column(nullable = false)
    private String replyContent;

    @Column(nullable = false)
    private String replyer;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
