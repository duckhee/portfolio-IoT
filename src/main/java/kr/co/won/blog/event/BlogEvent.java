package kr.co.won.blog.event;

import kr.co.won.blog.domain.BlogDomain;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * Blog Create or Update events
 */
public class BlogEvent extends ApplicationEvent {

    private BlogDomain blog;


    public BlogEvent(BlogDomain blog) {
        super(blog);
    }



}
