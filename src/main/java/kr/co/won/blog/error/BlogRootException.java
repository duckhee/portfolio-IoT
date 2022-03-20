package kr.co.won.blog.error;

import kr.co.won.blog.domain.BlogDomain;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BlogRootException extends RuntimeException {

    private BlogDomain blog;

    public BlogRootException(BlogDomain blog) {
        super();
        this.blog = blog;
    }

    public BlogRootException(BlogDomain blog, String msg) {
        super(blog.getTitle() + " " + msg);
        this.blog = blog;
    }
}
