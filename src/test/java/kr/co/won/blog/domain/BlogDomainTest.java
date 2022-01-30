package kr.co.won.blog.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class BlogDomainTest {

    @DisplayName(value = "01. blog builder Test")
    @Test
    void blogBuilderTests() {
        String title = "blog title";
        String content = "blog content";
        String email = "test@co.kr";
        String name = "test";
        BlogDomain blogBuilder = BlogDomain.builder()
                .writer(name)
                .writerEmail(email)
                .content(content)
                .title(title)
                .build();

        assertThat(blogBuilder.getTitle()).isEqualTo(title);
        assertThat(blogBuilder.getContent()).isEqualTo(content);
        assertThat(blogBuilder.getWriter()).isEqualTo(name);
        assertThat(blogBuilder.getWriterEmail()).isEqualTo(email);
        assertThat(blogBuilder.getReplies()).isNotNull();
    }

}