package kr.co.won.blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.TestUser;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.factory.BlogFactory;
import kr.co.won.blog.form.CreateReplyForm;
import kr.co.won.blog.persistence.BlogPersistence;
import kr.co.won.blog.persistence.BlogReplyPersistence;
import kr.co.won.config.RestDocsConfiguration;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.type.UserRoleType;
import kr.co.won.user.factory.UserFactory;
import kr.co.won.user.persistence.UserPersistence;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .replyContent("replyadfdsf")
                .build();
        log.info("get reply form ::: {}", replyForm.toString());
        mockMvc.perform(post("/api/blogs/{blogIdx}/reply", testBlog.getIdx())
                        .contentType(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(replyForm)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(print())
                .andDo(document("create-replies",
                        /** response content type */
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional()
                        ),
                        /** Request Header */
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("ACCEPT Header 값이다.").optional(),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional()
                        ),
                        /** path variable */
                        pathParameters(
                                parameterWithName("blogIdx").description("reply 을 추가할 블로그의 고유 번호 값")
                        ),
                        /** Response Body */
                        relaxedResponseFields(
                                fieldWithPath("idx").type(JsonFieldType.NUMBER).description("생성된 reply의 고유 번호 값").optional(),
                                fieldWithPath("replyer").type(JsonFieldType.STRING).description("현재 reply을 생성한 유저의 이름").optional(),
                                fieldWithPath("replyContent").type(JsonFieldType.STRING).description("현재 reply을 생성한 유저의 이메일 주소").optional(),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("현재 reply이 생성된 시간").optional(),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("현재 reply이 수정된 시간").optional()
                        ),
                        /** Blog Create Links */
                        links(
                                linkWithRel("self").description("현재 호출된 링크").optional(),
                                linkWithRel("profile").description("현재 호출된 API의 기능에 대해서 설명이 되어 있는 document를 볼 수 있는 링크이다.").optional()
                        )
                        ));
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

    @TestUser(authLevel = UserRoleType.USER)
    @DisplayName(value = "02. list reply Test - with USER ROLE")
    @Test
    void listReplyApiWithBoardTestsWithUser() throws Exception {
        UserDomain testUser = userFactory.testUser("tester123", "tester123@co.kr", "1234");
        BlogDomain testBlog = blogFactory.makeBlogWithReply("title", "content", testUser, 10);
        mockMvc.perform(get("/api/blogs/{blogIdx}/reply", testBlog.getIdx()))
                .andExpect(status().isOk())
                .andDo(print());
        BlogDomain findBlog = blogPersistence.findWithReplyByIdx(testBlog.getIdx()).get();
        assertThat(findBlog.getReplies().size()).isEqualTo(10);
    }
}