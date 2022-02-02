package kr.co.won.blog.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Entity
@Table(name = "tbl_blog_resources")
@Getter
@Setter
@ToString(exclude = {})
@EqualsAndHashCode(of = {"idx"})
@NoArgsConstructor
@AllArgsConstructor
public class BlogResourceDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false, length = 255)
    private String originalName;

    @Column(nullable = false, length = 255)
    private String saveFileName;

    @Column(nullable = false)
    private String fileSize;

    @Column(nullable = false)
    private String extension;

    @OnDelete(action = OnDeleteAction.CASCADE)
//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "blog_idx")
    private BlogDomain blog;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * blog resources function
     */

    /**
     * resource add blog
     */
    public void addBlog(BlogDomain blog) {
        this.blog = blog;
    }

    /**
     * resource remove blog
     */
    public void remove() {
        // TODO Blog set list remove add
        this.blog = null;
    }

    /**
     * save file name generate
     */
    public void generateSaveName() {
        String savedName = UUID.randomUUID().toString().replace("-", "");
        this.saveFileName = savedName + "." + this.extension;
    }

    /**
     * extension check
     */
    public boolean confirmExtension(String ext) {
        return this.extension.toLowerCase().equals(ext.toLowerCase());
    }

    public void confirmExtension(String... extension) {
        Arrays.stream(extension).anyMatch(ext->ext.toLowerCase().equals(this.extension.toLowerCase()));
    }
}
