package kr.co.won.blog.factory;

import kr.co.won.blog.persistence.BlogPersistence;
import kr.co.won.blog.persistence.BlogReplyPersistence;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@RequiredArgsConstructor
public class BlogFactory {

    private final UserPersistence userPersistence;

    private final BlogPersistence blogPersistence;

    private final BlogReplyPersistence blogReplyPersistence;
}
