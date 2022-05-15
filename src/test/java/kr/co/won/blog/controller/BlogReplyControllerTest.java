package kr.co.won.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.TestUser;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.domain.BlogReplyDomain;
import kr.co.won.blog.factory.BlogFactory;
import kr.co.won.blog.form.CreateReplyForm;
import kr.co.won.blog.persistence.BlogPersistence;
import kr.co.won.blog.persistence.BlogReplyPersistence;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.type.UserRoleType;
import kr.co.won.user.factory.UserFactory;
import kr.co.won.user.persistence.UserPersistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@AutoConfigureMockMvc
@Import(value = {UserFactory.class, BlogFactory.class})
class BlogReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserPersistence userPersistence;

    @Autowired
    private BlogPersistence blogPersistence;

    @Autowired
    private BlogReplyPersistence blogReplyPersistence;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private BlogFactory blogFactory;

    @AfterEach
    public void dataInit() {
        blogReplyPersistence.deleteAll();
        blogPersistence.deleteAll();
        userPersistence.deleteAll();
    }

    @TestUser(authLevel = UserRoleType.USER, email = "replyer@co.kr")
    @DisplayName(value = "01. create reply Tests")
    @Test
    public void createReplyTests() throws Exception {
        String replyerEmail = "replyer@co.kr";
        UserDomain findReplyer = userPersistence.findByEmail("replyer@co.kr").orElseThrow(() -> new IllegalArgumentException(""));
        CreateReplyForm form = CreateReplyForm.builder()
                .replyContent("testingReply")
                .build();
        String title = "testing";
        String content = "content";
        BlogDomain newBlog = blogFactory.makeBlog(replyerEmail, title, content);
        mockMvc.perform(post("/blogs/" + newBlog.getIdx() + "/reply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form))
                        .with(csrf())
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }

    @DisplayName(value = "02. list reply Tests")
    @Test
    void listRepliesTests() throws Exception {
        UserDomain testUser = userFactory.testUser("testing", "atesting@co.kr", "12346");
        BlogDomain makeBlog = blogFactory.makeBlogWithReply("test", "test", testUser, 10);
        mockMvc.perform(get("/blogs/" + makeBlog.getIdx() + "/reply")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName(value = "03. read reply Tests")
    @Test
    void readReplyTests() throws Exception {
        UserDomain testUser = userFactory.testUser("testing", "atesting@co.kr", "12346");
        BlogDomain makeBlog = blogFactory.makeBlogWithReply("test", "test", testUser, 10);
        List<BlogReplyDomain> findReplies = blogReplyPersistence.findByBlogIdx(makeBlog.getIdx());
        BlogReplyDomain findReply = findReplies.get(0);
        mockMvc.perform(get("/blogs/"+makeBlog.getIdx()+"/reply/"+findReply.getIdx())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}