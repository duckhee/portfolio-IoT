package kr.co.won.errors.custom.study;

public class StudyCreateException extends Exception {

    public StudyCreateException() {
        super();
    }

    public StudyCreateException(String message) {
        super(message);
    }

    public StudyCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public StudyCreateException(Throwable cause) {
        super(cause);
    }

    protected StudyCreateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
