package kr.co.won.blog.factory;

import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.persistence.BlogPersistence;
import kr.co.won.blog.persistence.BlogReplyPersistence;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;

@TestComponent
@Transactional
@RequiredArgsConstructor
public class BlogFactory {

    private final UserPersistence userPersistence;

    private final BlogPersistence blogPersistence;

    private final BlogReplyPersistence blogReplyPersistence;

    public BlogDomain makeBlog(UserDomain writer, String title, String content) {
        BlogDomain testBoard = BlogDomain.builder()
                .title(title)
                .content(content)
                .writerEmail(writer.getEmail())
                .writer(writer.getName())
                .build();
        BlogDomain savedBlog = blogPersistence.save(testBoard);
        return savedBlog;
    }
}
