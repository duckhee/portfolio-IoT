package kr.co.won.blog.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.assertj.core.api.Assertions.*;

/**
 * Blog Domain Unit Test
 */
class BlogDomainTest {

    @DisplayName(value = "01. blog builder Test")
    @Test
    void blogDomainBuilderTests() {
        String title = "blog title";
        String content = "blog content";
        String email = "test@co.kr";
        String name = "test";
        String uri = URI.create("github.com/portfolio/project").toString();
        BlogDomain blogBuilder = makeBlogDomain(title, content, email, name, uri);

        assertThat(blogBuilder.getTitle()).isEqualTo(title);
        assertThat(blogBuilder.getContent()).isEqualTo(content);
        assertThat(blogBuilder.getWriter()).isEqualTo(name);
        assertThat(blogBuilder.getWriterEmail()).isEqualTo(email);
        assertThat(blogBuilder.getReplies()).isNotNull();
    }

    @DisplayName(value = "02. blog domain reply add Test")
    @Test
    void blogReplyAddTests() {
        String title = "blog title";
        String content = "blog content";
        String email = "test@co.kr";
        String name = "test";
        String uri = URI.create("github.com/portfolio/project").toString();
        BlogDomain blogBuilder = makeBlogDomain(title, content, email, name, uri);

        BlogReplyDomain replyBuilder = makeReplyDomain(email, name, "reply content");
        blogBuilder.addReply(replyBuilder);

        assertThat(blogBuilder.getTitle()).isEqualTo(title);
        assertThat(blogBuilder.getViewCnt()).isEqualTo(0);
        assertThat(blogBuilder.getContent()).isEqualTo(content);
        assertThat(blogBuilder.getWriter()).isEqualTo(name);
        assertThat(blogBuilder.getWriterEmail()).isEqualTo(email);
        assertThat(blogBuilder.getReplies()).isNotNull();
        assertThat(blogBuilder.getReplies()).contains(replyBuilder);
        assertThat(blogBuilder.getReplies().size()).isEqualTo(1);
    }

    @DisplayName(value = "03. blog domain reply remove Test")
    @Test
    void blogReplyRemoveTests() {
        String title = "blog title";
        String content = "blog content";
        String email = "test@co.kr";
        String name = "test";
        String uri = URI.create("github.com/portfolio/project").toString();
        BlogDomain blogBuilder = makeBlogDomain(title, content, email, name, uri);

        BlogReplyDomain replyBuilder = makeReplyDomain(email, name, "reply content");
        blogBuilder.addReply(replyBuilder);

        assertThat(blogBuilder.getTitle()).isEqualTo(title);
        assertThat(blogBuilder.getViewCnt()).isEqualTo(0);
        assertThat(blogBuilder.getContent()).isEqualTo(content);
        assertThat(blogBuilder.getWriter()).isEqualTo(name);
        assertThat(blogBuilder.getWriterEmail()).isEqualTo(email);
        assertThat(blogBuilder.getReplies()).isNotNull();
        assertThat(blogBuilder.getReplies()).contains(replyBuilder);
        assertThat(blogBuilder.getReplies().size()).isEqualTo(1);

        blogBuilder.removeReply(replyBuilder);
        assertThat(blogBuilder.getReplies().size()).isEqualTo(0);
    }

    // make reply domain builder
    private BlogReplyDomain makeReplyDomain(String email, String name, String replyContent) {
        return BlogReplyDomain.builder()
                .replyer(name)
                .replyerEmail(email)
                .replyContent(replyContent)
                .build();
    }

    // make blog domain builder
    private BlogDomain makeBlogDomain(String title, String content, String email, String name, String uri) {
        return BlogDomain.builder()
                .writer(name)
                .writerEmail(email)
                .content(content)
                .title(title)
                .projectUrl(uri)
                .build();
    }


}