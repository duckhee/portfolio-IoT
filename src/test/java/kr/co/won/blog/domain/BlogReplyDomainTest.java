package kr.co.won.blog.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class BlogReplyDomainTest {

    @DisplayName(value = "01. blog reply domain builder Test")
    @Test
    void createReplyBuilderTests() {
        String replyContent = "replyContent";
        String replyer = "tester";
        String email = "tester@co.kr";
        BlogReplyDomain buildReply = buildReply(replyContent, replyer, email);

        assertThat(buildReply.getReplyContent()).isEqualTo(replyContent);
        assertThat(buildReply.getReplyer()).isEqualTo(replyer);
        assertThat(buildReply.getReplyerEmail()).isEqualTo(email);
    }




    private BlogReplyDomain buildReply(String replyContent, String replyer, String email) {
        BlogReplyDomain buildReply = BlogReplyDomain.builder()
                .replyContent(replyContent)
                .replyer(replyer)
                .replyerEmail(email)
                .build();
        return buildReply;
    }
}