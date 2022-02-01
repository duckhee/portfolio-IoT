package kr.co.won.blog.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NamedEntityGraph(name = "blog.withReply",
        attributeNodes = {@NamedAttributeNode(value = "replies")}
)

@NamedEntityGraph(name = "blog.withReplyAndResource",
        attributeNodes = {@NamedAttributeNode(value = "replies")}
)

@Entity
@Table(name = "tbl_blog")
@Getter
@Setter
@Builder
@ToString(exclude = {"replies"})
@EqualsAndHashCode(of = {"idx"})
@NoArgsConstructor
@AllArgsConstructor
public class BlogDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String writerEmail;

    @Lob
    private String content;

    private String projectUri;

    @Builder.Default
    @Column(nullable = false)
    private Long viewCnt = 0L;

    @Builder.Default
    @OneToMany(mappedBy = "blog", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<BlogReplyDomain> replies = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Blog Function
     */


    /**
     * add reply
     */
    public void addReply(BlogReplyDomain reply) {
        this.replies.add(reply);
        reply.setBlog(this);
    }

    /**
     * add replies
     */
    public void addReply(BlogReplyDomain... replies) {
        List<BlogReplyDomain> newReplies = Arrays.stream(replies).collect(Collectors.toList());
        this.replies.addAll(newReplies);
        newReplies.forEach(reply -> reply.setBlog(this));
    }

    /**
     * add replies
     */
    public void addReplies(List<BlogReplyDomain> replies) {
        this.replies.addAll(replies);
        replies.forEach(reply -> reply.setBlog(this));
    }

    /**
     * remove reply
     */
    public void removeReply(BlogReplyDomain reply) {
        this.replies.remove(reply);
        reply.setBlog(null);
    }

    /**
     * remove replies
     */
    public void removeReply(BlogReplyDomain... replies) {
        List<BlogReplyDomain> deleteReplies = Arrays.stream(replies).collect(Collectors.toList());
        this.replies.removeAll(deleteReplies);
        deleteReplies.forEach(blogReplyDomain -> blogReplyDomain.setBlog(null));
    }

    /**
     * blog writer
     */
    public boolean isOwner(String email) {
        return this.writerEmail.equals(email);
    }


}
