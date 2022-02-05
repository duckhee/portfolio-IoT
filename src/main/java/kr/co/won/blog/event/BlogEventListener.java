package kr.co.won.blog.event;

import kr.co.won.blog.persistence.BlogPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * This is Blog Event Listener
 */

@Component
@RequiredArgsConstructor
public class BlogEventListener {

    private final BlogPersistence blogPersistence;


    @EventListener(value = {BlogEvent.class})
    public void blogEventListener(BlogEvent blog) {

    }

}
