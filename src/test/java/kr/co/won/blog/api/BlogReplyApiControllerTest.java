package kr.co.won.blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.auth.TestUser;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.factory.BlogFactory;
import kr.co.won.blog.form.CreateReplyForm;
import kr.co.won.blog.persistence.BlogPersistence;
import kr.co.won.blog.persistence.BlogReplyPersistence;
import kr.co.won.config.RestDocsConfiguration;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.factory.UserFactory;
import kr.co.won.user.persistence.UserPersistence;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(value = {RestDocsConfiguration.class, UserFactory.class, BlogFactory.class})
class BlogReplyApiControllerTest {

    private final String HAL_JSON = "HAL JSON";

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserPersistence userPersistence;

    @Autowired
    private BlogPersistence blogPersistence;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private BlogFactory blogFactory;

    @Autowired
    private BlogReplyPersistence replyPersistence;

    @AfterEach
    public void testDataInit() {
        userPersistence.deleteAll();
        replyPersistence.deleteAll();
        blogPersistence.deleteAll();
    }

    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "01. create reply Test")
    @Test
    void createReplyApiTests() throws Exception {
        UserDomain testUser = userFactory.testUser("tester123", "tester123@co.kr", "1234");
        BlogDomain testBlog = blogFactory.makeBlog(testUser, "title", "content");
        CreateReplyForm replyForm = CreateReplyForm.builder()
                .blogIdx(testBlog.getIdx())
                .replyContent("replyadfdsf")
                .build();
        log.info("get reply form ::: {}", replyForm.toString());
        mockMvc.perform(post("/api/blogs/{blogIdx}/reply", testBlog.getIdx())
                        .contentType(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(replyForm)))
                .andExpect(status().isOk())
                .andDo(print());
        BlogDomain findBlog = blogPersistence.findWithReplyByIdx(testBlog.getIdx()).get();

        assertThat(findBlog.getReplies().size()).isEqualTo(1);
    }

    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "02. list reply Test")
    @Test
    void listReplyApiWithBoardTests() throws Exception {
        UserDomain testUser = userFactory.testUser("tester123", "tester123@co.kr", "1234");
        BlogDomain testBlog = blogFactory.makeBlogWithReply("title", "content", testUser, 10);
        mockMvc.perform(get("/api/blogs/{blogIdx}/reply", testBlog.getIdx()))
                .andExpect(status().isOk())
                .andDo(print());
        BlogDomain findBlog = blogPersistence.findWithReplyByIdx(testBlog.getIdx()).get();
        assertThat(findBlog.getReplies().size()).isEqualTo(10);
    }
}