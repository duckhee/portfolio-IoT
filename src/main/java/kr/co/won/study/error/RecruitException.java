package kr.co.won.study.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class RecruitException extends RuntimeException {

    private String msg;

    public RecruitException() {
        super();
    }

    public RecruitException(String message) {
        super(message);
        this.msg = message;
    }

    public RecruitException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecruitException(Throwable cause) {
        super(cause);
    }

    protected RecruitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
