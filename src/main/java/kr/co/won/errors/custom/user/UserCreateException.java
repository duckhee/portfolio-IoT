package kr.co.won.errors.custom.user;

public class
UserCreateException extends Exception {

    public UserCreateException() {
        super();
    }

    public UserCreateException(String message) {
        super(message);
    }

    public UserCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserCreateException(Throwable cause) {
        super(cause);
    }

    protected UserCreateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
