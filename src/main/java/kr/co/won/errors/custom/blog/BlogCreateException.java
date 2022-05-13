package kr.co.won.errors.custom.blog;

import kr.co.won.blog.domain.BlogDomain;
import lombok.Getter;

/**
 * File Name        : BlogCreateException
 * Author           : Doukhee Won
 * Date             : 2022/05/13
 * Version          : v0.0.1
 * <p>
 * Blog Error Exception
 */
@Getter
public class BlogCreateException extends Exception {

    private BlogDomain blog;

    public BlogCreateException() {
        super();
        this.blog = null;
    }

    public BlogCreateException(BlogDomain blog) {
        super();
        this.blog = blog;
    }

    public BlogCreateException(String message) {
        super(message);
    }

    public BlogCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlogCreateException(Throwable cause) {
        super(cause);
    }

    protected BlogCreateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
