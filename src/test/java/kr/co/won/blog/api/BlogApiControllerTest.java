package kr.co.won.blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.TestUser;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.factory.BlogFactory;
import kr.co.won.blog.form.CreateBlogForm;
import kr.co.won.blog.persistence.BlogPersistence;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.factory.UserFactory;
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
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)
@AutoConfigureRestDocs
@Import(value = {RestDocsAutoConfiguration.class, UserFactory.class, BlogFactory.class})
class BlogApiControllerTest {

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

    @AfterEach
    public void testDataInit() {
        userPersistence.deleteAll();
        blogPersistence.deleteAll();
    }

    @TestUser(authLevel = UserRoleType.USER)
    @DisplayName(value = "01. Blog create api Test ")
    @Test
    void createBlogTests() throws Exception {
        CreateBlogForm blogForm = CreateBlogForm.builder()
                .title("testing")
                .content("content")
                .projectUrl(new URL("http://github.com/project/uri"))
                .build();

        mockMvc.perform(post("/api/blogs")
                        .contentType(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(blogForm)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.list-blogs").exists())
                .andExpect(jsonPath("_links.query-blogs").exists())
                .andExpect(jsonPath("_links.update-blogs").exists())
                .andExpect(jsonPath("_links.delete-blogs").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(print())
                .andDo(document("create-blogs",
                        /** response content type */
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional(),
                                headerWithName(HttpHeaders.LOCATION).description("생성된 블로그에 대한 정보를 볼 수 있는 링크").optional()
                        ),
                        /** Request Header */
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("ACCEPT Header 값이다.").optional(),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional()
                        ),
                        /** Request Body */
                        relaxedRequestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("생성할 블로그의 타이틀").optional(),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("생성할 블로그의 글").optional(),
                                fieldWithPath("projectUri").type(JsonFieldType.STRING).description("생성할 블로그에 대한 주소").optional()
                        ),
                        /** Response Body */
                        relaxedResponseFields(
                                fieldWithPath("idx").type(JsonFieldType.NUMBER).description("생성된 블로그의 고유 번호").optional(),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("생성된 블로그의 타이틀").optional(),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("생성된 블로그의 글").optional(),
                                fieldWithPath("writer").type(JsonFieldType.STRING).description("생성한 블로그를 작성한 사람의 이름").optional(),
                                fieldWithPath("writerEmail").type(JsonFieldType.STRING).description("생성한 블로그를 작성한 사람의 이메일").optional(),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성한 블로그를 작성한 시간").optional(),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("생성한 블로그를 수정된 시간").optional(),
                                fieldWithPath("replies").type(JsonFieldType.ARRAY).description("생성한 블로그에 달려 있는 리플").optional()
                        ),
                        /** Blog Create Links */
                        links(
                                linkWithRel("self").description("현재 호출된 링크").optional(),
                                linkWithRel("profile").description("현재 호출된 API의 기능에 대해서 설명이 되어 있는 document를 볼 수 있는 링크이다.").optional(),
                                linkWithRel("list-blogs").description("블로그의 목록을 볼 수 있는 링크").optional(),
                                linkWithRel("query-blogs").description("생성된 블로그에 대한 정보를 볼 수 있는 링크").optional(),
                                linkWithRel("update-blogs").description("생성된 블로그를 업데이트할 수 있는 링크").optional(),
                                linkWithRel("delete-blogs").description("생성된 블로그를 삭제를 할 수 있는 링크").optional()
                        )
                ));
    }

    @TestUser(authLevel = UserRoleType.USER)
    @DisplayName(value = "02. blog find api Test")
    @Test
    void findBlogTests() throws Exception {
        UserDomain testUser = userFactory.testUser("testinguser", "testinguser@co.kr", "1234");
        BlogDomain testBlog = blogFactory.makeBlogWithReply("title", "content", testUser, 10);
        mockMvc.perform(get("/api/blogs/{idx}", testBlog.getIdx()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @TestUser(authLevel = UserRoleType.USER)
    @DisplayName(value = "03. list blog api Test")
    @Test
    void listBlogTests() throws Exception {

    }
}