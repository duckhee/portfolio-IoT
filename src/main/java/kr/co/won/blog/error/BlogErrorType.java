package kr.co.won.blog.error;

public enum BlogErrorType {

    CREATED_ERROR(1L, "create blog error"),
    UPDATED_ERROR(2L, "update blog error"),
    FIND_ERROR(3L, "find blog error"),
    DELETE_ERROR(4L, "delete blog error"),
    VALIDATED_ERROR(5L, "validation error");

    private final Long errorCode;
    private final String msg;


    BlogErrorType(Long errorCode, String errMsg) {
        this.errorCode = errorCode;
        this.msg = errMsg;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    public String getMsg() {
        return msg;
    }
}
