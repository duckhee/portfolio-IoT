package kr.co.won.blog.factory;

import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.domain.BlogReplyDomain;
import kr.co.won.blog.persistence.BlogPersistence;
import kr.co.won.blog.persistence.BlogReplyPersistence;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    public BlogDomain makeBlog(String writer, String title, String content) {
        BlogDomain testBlog = BlogDomain.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .projectUrl("http://github.com/tester/test-project")
                .writerEmail(writer + "@co.kr")
                .build();
        BlogDomain savedBlog = blogPersistence.save(testBlog);
        return savedBlog;
    }

    public BlogDomain makeBlogWithReply(String title, String content, UserDomain user) {
        BlogDomain testBlog = makeBlog(user, title, content);
        BlogReplyDomain testReply = BlogReplyDomain.builder()
                .replyer("replyer")
                .replyerEmail("replyer@co.kr")
                .replyContent("reply")
                .build();
        testBlog.addReply(testReply);
        BlogDomain savedBlog = blogPersistence.save(testBlog);
        return savedBlog;
    }

    public BlogDomain makeBlogWithReply(String title, String content, UserDomain user, int replyNumber) {
        BlogDomain testBlog = makeBlog(user, title, content);
        List<BlogReplyDomain> replies = new ArrayList<>();
        for (int i = 0; i < replyNumber; i++) {
            BlogReplyDomain testReply = BlogReplyDomain.builder()
                    .replyer("replyer" + i)
                    .replyerEmail("replyer" + i + "@co.kr")
                    .replyContent("reply" + i)
                    .build();
            replies.add(testReply);
        }
        testBlog.addReplies(replies);
        BlogDomain savedBlog = blogPersistence.save(testBlog);
        return savedBlog;
    }


    public List<BlogDomain> makeMockBulkBlogWithReply(int boardLength, String title, String content, UserDomain user, int replyNumber) {
        List<BlogDomain> list = new ArrayList<>();
        for (int i = 0; i < boardLength; i++) {
            BlogDomain blog = makeBlogWithReply(title + i, content + i, user, replyNumber);
            list.add(blog);
        }
        return list;
    }
}
