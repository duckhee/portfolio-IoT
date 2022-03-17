package kr.co.won.errors.custom.blog;

public class BlogCreateException extends Exception {

    public BlogCreateException() {
        super();
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
