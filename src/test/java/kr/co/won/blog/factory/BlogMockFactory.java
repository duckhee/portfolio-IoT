package kr.co.won.blog.factory;

import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.domain.BlogReplyDomain;
import kr.co.won.user.domain.UserDomain;
import org.springframework.boot.test.context.TestComponent;

import java.util.ArrayList;
import java.util.List;

@TestComponent
public class BlogMockFactory {


    public BlogDomain makeMockBlog(String title, String writer, String content) {
        BlogDomain build = BlogDomain.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .writerEmail(writer + "@co.kr")
                .projectUrl(title)
                .build();
        return build;
    }

    public BlogDomain makeMockBlog(String title, String writer, String content, String projectUrl) {
        BlogDomain build = BlogDomain.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .writerEmail(writer + "@co.kr")
                .projectUrl(projectUrl)
                .build();
        return build;
    }


    public BlogDomain makeMockBlog(String title, String writer, String writerEmail, String content, String projectUrl) {
        BlogDomain build = BlogDomain.builder()
                .title(title)
                .writer(writer)
                .writerEmail(writerEmail)
                .content(content)
                .projectUrl(projectUrl)
                .build();
        return build;
    }


    public BlogDomain makeBlogWithReply(String title, String content, UserDomain user, int replyNumber) {
        BlogDomain testBlog = makeMockBlog(user.getName(), title, content);
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
        return testBlog;
    }
}
