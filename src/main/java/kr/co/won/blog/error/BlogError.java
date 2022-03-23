package kr.co.won.blog.error;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogError {

    private String uri;

    private String defaultErrorMsg;

    private String message;


    public BlogError(String uri, Exception exception) {
        if (exception instanceof BlogRootException) {
            this.uri = uri;
            BlogRootException blogRootException = (BlogRootException) exception;
            // error message setting
            this.defaultErrorMsg = blogRootException.getBlog().getTitle() + " " + blogRootException.getLocalizedMessage();
            this.message = exception.getLocalizedMessage();
            return;
        }
        log.warn("blog error not current exception");
        this.uri = uri;
        this.message = exception.getLocalizedMessage();
    }
}
