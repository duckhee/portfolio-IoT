package kr.co.won.blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.TestUser;
import kr.co.won.blog.form.CreateBlogForm;
import kr.co.won.blog.persistence.BlogPersistence;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.persistence.UserPersistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)
@AutoConfigureRestDocs
@Import(value = {RestDocsAutoConfiguration.class})
class BlogApiControllerTest {

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

    @AfterEach
    public void testDataInit(){
        userPersistence.deleteAll();
        blogPersistence.deleteAll();
    }

    @TestUser(authLevel = UserRoleType.USER)
    @DisplayName(value = "01. create Blog Test ")
    @Test
    void createBlogTests() throws Exception {
        CreateBlogForm blogForm = CreateBlogForm.builder()
                .title("testing")
                .content("content")
                .projectUri(URI.create("github.com/project/uri"))
                .build();
        mockMvc.perform(post("/api/blogs")
                        .contentType(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(blogForm)))
                .andDo(print());
    }
}