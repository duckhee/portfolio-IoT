package kr.co.won.blog.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogError {

    private String uri;

//    private String name;

    private String message;


    public BlogError(String uri, Exception exception) {
        this.uri = uri;
//        this.name = exception.getClass().getName();
        this.message = exception.getLocalizedMessage();
    }
}
