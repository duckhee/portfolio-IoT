package kr.co.won.blog.event;

import kr.co.won.blog.domain.BlogDomain;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * Blog Create or Update events
 */
@Getter
public class BlogEvent extends ApplicationEvent {

    private final BlogDomain blog;

    public BlogEvent(BlogDomain blog) {
        super(blog);
        this.blog = blog;
    }


}
