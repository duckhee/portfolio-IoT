package kr.co.won.blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.TestUser;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.factory.BlogFactory;
import kr.co.won.blog.form.BlogForm;
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

import java.net.URL;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
        BlogForm blogForm = BlogForm.builder()
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
                                fieldWithPath("projectUrl").type(JsonFieldType.STRING).description("생성할 블로그에 대한 주소").optional()
                        ),
                        /** Response Body */
                        relaxedResponseFields(
                                fieldWithPath("idx").type(JsonFieldType.NUMBER).description("생성된 블로그의 고유 번호").optional(),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("생성된 블로그의 타이틀").optional(),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("생성된 블로그의 글").optional(),
                                fieldWithPath("writer").type(JsonFieldType.STRING).description("생성한 블로그를 작성한 사람의 이름").optional(),
                                fieldWithPath("writerEmail").type(JsonFieldType.STRING).description("생성한 블로그를 작성한 사람의 이메일").optional(),
                                fieldWithPath("projectUri").type(JsonFieldType.STRING).description("생성한 블로그에 연결되어 있는 프로젝트의 주소").optional(),
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

    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "02. blog find api Test- ROLE ADMIN")
    @Test
    void findBlogTestsWithAdmin() throws Exception {
        UserDomain testUser = userFactory.testUser("testinguser", "testinguser@co.kr", "1234");
        BlogDomain testBlog = blogFactory.makeBlogWithReply("title", "content", testUser, 10);
        mockMvc.perform(get("/api/blogs/{idx}", testBlog.getIdx()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.list-blogs").exists())
                .andExpect(jsonPath("_links.create-blogs").exists())
                .andExpect(jsonPath("_links.update-blogs").exists())
                .andExpect(jsonPath("_links.delete-blogs").exists())
                .andDo(print())
                .andDo(document("read-blogs-admin",
                        /** response content type */
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional()
                        ),
                        /** Request Header */
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("ACCEPT Header 값이다.").optional(),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional()
                        )
                        ,
                        /** path variable */
                        pathParameters(
                                parameterWithName("idx").description("읽어올 블로그의 고유 번호 값")
                        ),
                        /** Response Body */
                        relaxedResponseFields(
                                fieldWithPath("idx").type(JsonFieldType.NUMBER).description("블로그의 고유 번호").optional(),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("블로그의 타이틀").optional(),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("블로그의 글").optional(),
                                fieldWithPath("writer").type(JsonFieldType.STRING).description("블로그를 작성한 사람의 이름").optional(),
                                fieldWithPath("writerEmail").type(JsonFieldType.STRING).description("블로그를 작성한 사람의 이메일").optional(),
                                fieldWithPath("projectUri").type(JsonFieldType.STRING).description("블로그에 연결되어 있는 프로젝트의 주소").optional(),
                                fieldWithPath("viewCnt").type(JsonFieldType.NUMBER).description("블로그를 사용자가 본 횟수").optional(),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("블로그를 작성한 시간").optional(),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("블로그를 수정된 시간").optional(),
                                fieldWithPath("replies").type(JsonFieldType.ARRAY).description("블로그에 달려 있는 리플").optional()
                        ),
                        /** Blog Create Links */
                        links(
                                linkWithRel("self").description("현재 호출된 링크").optional(),
                                linkWithRel("profile").description("현재 호출된 API의 기능에 대해서 설명이 되어 있는 document를 볼 수 있는 링크이다.").optional(),
                                linkWithRel("list-blogs").description("블로그의 목록을 볼 수 있는 링크").optional(),
                                linkWithRel("query-blogs").description("블로그를 볼수 있는 링크").optional(),
                                linkWithRel("create-blogs").description("블로그를 새로 만들 수 있는 링크").optional(),
                                linkWithRel("update-blogs").description("블로그를 업데이트할 수 있는 링크").optional(),
                                linkWithRel("delete-blogs").description("블로그를 삭제를 할 수 있는 링크").optional()
                        )
                ));
    }

    @TestUser(authLevel = UserRoleType.USER)
    @DisplayName(value = "02. blog find api Test - ROLE USER")
    @Test
    void findBlogTestsWithUser() throws Exception {
        UserDomain testUser = userFactory.testUser("testinguser", "testinguser@co.kr", "1234");
        BlogDomain testBlog = blogFactory.makeBlogWithReply("title", "content", testUser, 10);
        mockMvc.perform(get("/api/blogs/{idx}", testBlog.getIdx()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(print())
                .andDo(document("read-blogs",
                        /** response content type */
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional()
                        ),
                        /** Request Header */
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("ACCEPT Header 값이다.").optional(),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional()
                        )
                        ,
                        /** path variable */
                        pathParameters(
                                parameterWithName("idx").description("읽어올 블로그의 고유 번호 값")
                        ),
                        /** Response Body */
                        relaxedResponseFields(
                                fieldWithPath("idx").type(JsonFieldType.NUMBER).description("블로그의 고유 번호").optional(),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("블로그의 타이틀").optional(),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("블로그의 글").optional(),
                                fieldWithPath("writer").type(JsonFieldType.STRING).description("블로그를 작성한 사람의 이름").optional(),
                                fieldWithPath("writerEmail").type(JsonFieldType.STRING).description("블로그를 작성한 사람의 이메일").optional(),
                                fieldWithPath("projectUri").type(JsonFieldType.STRING).description("블로그에 연결되어 있는 프로젝트의 주소").optional(),
                                fieldWithPath("viewCnt").type(JsonFieldType.NUMBER).description("블로그를 사용자가 본 횟수").optional(),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("블로그를 작성한 시간").optional(),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("블로그를 수정된 시간").optional(),
                                fieldWithPath("replies").type(JsonFieldType.ARRAY).description("블로그에 달려 있는 리플").optional()
                        ),
                        /** Blog Create Links */
                        links(
                                linkWithRel("self").description("현재 호출된 링크").optional(),
                                linkWithRel("profile").description("현재 호출된 API의 기능에 대해서 설명이 되어 있는 document를 볼 수 있는 링크이다.").optional(),
                                linkWithRel("list-blogs").description("블로그의 목록을 볼 수 있는 링크").optional(),
                                linkWithRel("query-blogs").description("블로그를 자세히 볼 수 있는 링크").optional(),
                                linkWithRel("create-blogs").description("블로그를 새로 만들 수 있는 링크").optional(),
                                linkWithRel("update-blogs").description("블로그를 업데이트할 수 있는 링크").optional(),
                                linkWithRel("delete-blogs").description("블로그를 삭제를 할 수 있는 링크").optional()
                        )
                ));
    }

    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "03. list blog api Test - ROLE ADMIN")
    @Test
    void listBlogTestsWithAdmin() throws Exception {
        UserDomain testUser = userFactory.testUser("testinguser", "testinguser@co.kr", "1234");
        blogFactory.makeMockBulkBlogWithReply(100, "title", "content", testUser, 1);
        mockMvc.perform(get("/api/blogs")
                        .contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("list-blogs",
                        /** response content type */
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional()
                        ),
                        /** Request Header */
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("ACCEPT Header 값이다.").optional(),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional()
                        ),
                        /** paging request query parameter */
                        relaxedRequestParameters(
                                parameterWithName("page").description("페이지 번호").optional(),
                                parameterWithName("size").description("한 페이지에서 몇개를 가져올 것인지 크기 설정").optional(),
                                parameterWithName("type").description("검색할 category 입력 ").optional(),
                                parameterWithName("keyword").description("검색할 문자열").optional()
                        ),
                        /** response body */
                        relaxedResponseFields(
                                fieldWithPath("_embedded.blogs").type(JsonFieldType.ARRAY).description("블로그에 대한 정보를 담고 있는 배열").optional(),
                                fieldWithPath("_embedded.blogs[].idx").type(JsonFieldType.NUMBER).description("블로그에 대한 고유 번호").optional(),
                                fieldWithPath("_embedded.blogs[].title").type(JsonFieldType.STRING).description("블로그의 타이틀").optional(),
                                fieldWithPath("_embedded.blogs[].content").type(JsonFieldType.STRING).description("블로그의 내용").optional(),
                                fieldWithPath("_embedded.blogs[].writer").type(JsonFieldType.STRING).description("블로그를 작성한 사용자의 이름").optional(),
                                fieldWithPath("_embedded.blogs[].writerEmail").type(JsonFieldType.STRING).description("블로그를 작성한 사용자의 이메일").optional(),
                                fieldWithPath("_embedded.blogs[].projectUri").type(JsonFieldType.STRING).description("블로그에 작성한 프로젝트의 URL").optional(),
                                fieldWithPath("_embedded.blogs[].viewCnt").type(JsonFieldType.NUMBER).description("블로그를 본 횟수").optional(),
                                fieldWithPath("_embedded.blogs[].createdAt").type(JsonFieldType.STRING).description("블로그가 생성된 시간").optional(),
                                fieldWithPath("_embedded.blogs[].updatedAt").type(JsonFieldType.STRING).description("블로그가 수정된 시간").optional(),
                                fieldWithPath("_embedded.blogs[].replies").type(JsonFieldType.ARRAY).description("블로그에 작성된 리플을 담고 있는 배열").optional(),
                                fieldWithPath("_embedded.blogs[].replies[].idx").type(JsonFieldType.NUMBER).description("블로그에 달린 리플에 대한 고유 번호").optional(),
                                fieldWithPath("_embedded.blogs[].replies[].replyer").type(JsonFieldType.STRING).description("블로그에 달린 리플을 단 사용자의 이름").optional(),
                                fieldWithPath("_embedded.blogs[].replies[].replyContent").type(JsonFieldType.STRING).description("블로그에 달린 리플을 단 사용자의 이름").optional(),
                                fieldWithPath("_embedded.blogs[].replies[].createdAt").type(JsonFieldType.STRING).description("블로그에 달린 리플이 생성된 시간").optional(),
                                fieldWithPath("_embedded.blogs[].replies[].updatedAt").type(JsonFieldType.STRING).description("블로그에 달린 리플이 수정된 시간").optional()
                        ),
                        /** user create links */
                        relaxedLinks(
                                linkWithRel("self").description("현재 호출된 링크").optional(),
                                linkWithRel("profile").description("현재 호출된 API의 기능에 대한 설명 되어 있는 document를 볼 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.blogs[].self").description("해당되는 블로그에 대해서 상세히 볼 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.blogs[].self.type").description("해당되는 블로그에 대해서 상세히 볼 수 있는 링크를 호출 할 수 있는 Http Method").optional(),
//                                linkWithRel("_embedded.users[].query-blogs").description("해당되는 블로그에 대해서 상세히 볼 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.blogs[].update-blogs").description("해당되는 블로그에 대해서 수정을 할 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.blogs[].update-blogs.type").description("해당되는 블로그에 대해서 상세히 볼 수 있는 링크를 호출 할 수 있는 Http Method").optional(),
                                linkWithRel("_embedded.blogs[].delete-blogs").description("해당되는 블로그에 대해서 삭제를 할 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.blogs[].delete-blogs.type").description("해당되는 블로그에 대해서 상세히 볼 수 있는 링크를 호출 할 수 있는 Http Method").optional(),
                                linkWithRel("_embedded.blogs[].replies[].self").description("해당되는 블로그에 달린 리플에 대해서 상세히 볼 수 있는 링크이다.").optional(),
//                                linkWithRel("_embedded.blogs[].replies[].self.type").description("해당되는 블로그에 달린 리플에 상세히 볼 수 있는 링크를 호출 할 수 있는 Http Method").optional(),
                                linkWithRel("_embedded.blogs[].replies[].query-reply").description("해당되는 블로그에 달린 리플에 대해서 상세히 볼 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.blogs[].replies[].query-reply.type").description("해당되는 블로그에 달린 리플에 대해서 상세히 볼 수 있는 링크를 호출 할 수 있는 Http Method").optional(),
                                linkWithRel("_embedded.blogs[].replies[].update-reply").description("해당되는 블로그에 달린 리플에 대해서 수정을 할 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.blogs[].replies[].update-reply.type").description("해당되는 블로그에 달린 리플에 대해서 상세히 볼 수 있는 링크를 호출 할 수 있는 Http Method").optional(),
                                linkWithRel("_embedded.blogs[].replies[].delete-reply").description("해당되는 블로그에 달린 리플에 대해서 삭제를 할 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.blogs[].replies[].delete-reply.type").description("해당되는 블로그에 달린 리플에 대해서 상세히 볼 수 있는 링크를 호출 할 수 있는 Http Method").optional()
                        )
                ));
    }

    @TestUser(authLevel = UserRoleType.USER)
    @DisplayName(value = "03. list blog api Test - ROLE USER")
    @Test
    void listBlogTestsWithUser() throws Exception {
        UserDomain testUser = userFactory.testUser("testinguser", "testinguser@co.kr", "1234");
        blogFactory.makeMockBulkBlogWithReply(100, "title", "content", testUser, 1);
        mockMvc.perform(get("/api/blogs")
                        .contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("list-blogs",
                        /** response content type */
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional()
                        ),
                        /** Request Header */
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("ACCEPT Header 값이다.").optional(),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional()
                        ),
                        /** paging request query parameter */
                        relaxedRequestParameters(
                                parameterWithName("page").description("페이지 번호").optional(),
                                parameterWithName("size").description("한 페이지에서 몇개를 가져올 것인지 크기 설정").optional(),
                                parameterWithName("type").description("검색할 category 입력 ").optional(),
                                parameterWithName("keyword").description("검색할 문자열").optional()
                        ),
                        /** response body */
                        relaxedResponseFields(
                                fieldWithPath("_embedded.blogs").type(JsonFieldType.ARRAY).description("블로그에 대한 정보를 담고 있는 배열").optional(),
                                fieldWithPath("_embedded.blogs[].idx").type(JsonFieldType.NUMBER).description("블로그에 대한 고유 번호").optional(),
                                fieldWithPath("_embedded.blogs[].title").type(JsonFieldType.STRING).description("블로그의 타이틀").optional(),
                                fieldWithPath("_embedded.blogs[].content").type(JsonFieldType.STRING).description("블로그의 내용").optional(),
                                fieldWithPath("_embedded.blogs[].writer").type(JsonFieldType.STRING).description("블로그를 작성한 사용자의 이름").optional(),
                                fieldWithPath("_embedded.blogs[].writerEmail").type(JsonFieldType.STRING).description("블로그를 작성한 사용자의 이메일").optional(),
                                fieldWithPath("_embedded.blogs[].projectUri").type(JsonFieldType.STRING).description("블로그에 작성한 프로젝트의 URL").optional(),
                                fieldWithPath("_embedded.blogs[].viewCnt").type(JsonFieldType.NUMBER).description("블로그를 본 횟수").optional(),
                                fieldWithPath("_embedded.blogs[].createdAt").type(JsonFieldType.STRING).description("블로그가 생성된 시간").optional(),
                                fieldWithPath("_embedded.blogs[].updatedAt").type(JsonFieldType.STRING).description("블로그가 수정된 시간").optional(),
                                fieldWithPath("_embedded.blogs[].replies").type(JsonFieldType.ARRAY).description("블로그에 작성된 리플을 담고 있는 배열").optional(),
                                fieldWithPath("_embedded.blogs[].replies[].idx").type(JsonFieldType.NUMBER).description("블로그에 달린 리플에 대한 고유 번호").optional(),
                                fieldWithPath("_embedded.blogs[].replies[].replyer").type(JsonFieldType.STRING).description("블로그에 달린 리플을 단 사용자의 이름").optional(),
                                fieldWithPath("_embedded.blogs[].replies[].replyContent").type(JsonFieldType.STRING).description("블로그에 달린 리플을 단 사용자의 이름").optional(),
                                fieldWithPath("_embedded.blogs[].replies[].createdAt").type(JsonFieldType.STRING).description("블로그에 달린 리플이 생성된 시간").optional(),
                                fieldWithPath("_embedded.blogs[].replies[].updatedAt").type(JsonFieldType.STRING).description("블로그에 달린 리플이 수정된 시간").optional()
                        ),
                        /** user create links */
                        relaxedLinks(
                                linkWithRel("self").description("현재 호출된 링크").optional(),
                                linkWithRel("profile").description("현재 호출된 API의 기능에 대한 설명 되어 있는 document를 볼 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.blogs[].self").description("해당되는 블로그에 대해서 상세히 볼 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.blogs[].self.type").description("해당되는 블로그에 대해서 상세히 볼 수 있는 링크를 호출 할 수 있는 Http Method").optional(),
//                                linkWithRel("_embedded.users[].query-blogs").description("해당되는 블로그에 대해서 상세히 볼 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.blogs[].update-blogs").description("해당되는 블로그에 대해서 수정을 할 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.blogs[].update-blogs.type").description("해당되는 블로그에 대해서 상세히 볼 수 있는 링크를 호출 할 수 있는 Http Method").optional(),
                                linkWithRel("_embedded.blogs[].delete-blogs").description("해당되는 블로그에 대해서 삭제를 할 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.blogs[].delete-blogs.type").description("해당되는 블로그에 대해서 상세히 볼 수 있는 링크를 호출 할 수 있는 Http Method").optional(),
                                linkWithRel("_embedded.blogs[].replies[].self").description("해당되는 블로그에 달린 리플에 대해서 상세히 볼 수 있는 링크이다.").optional(),
//                                linkWithRel("_embedded.blogs[].replies[].self.type").description("해당되는 블로그에 달린 리플에 상세히 볼 수 있는 링크를 호출 할 수 있는 Http Method").optional(),
                                linkWithRel("_embedded.blogs[].replies[].query-reply").description("해당되는 블로그에 달린 리플에 대해서 상세히 볼 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.blogs[].replies[].query-reply.type").description("해당되는 블로그에 달린 리플에 대해서 상세히 볼 수 있는 링크를 호출 할 수 있는 Http Method").optional(),
                                linkWithRel("_embedded.blogs[].replies[].update-reply").description("해당되는 블로그에 달린 리플에 대해서 수정을 할 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.blogs[].replies[].update-reply.type").description("해당되는 블로그에 달린 리플에 대해서 상세히 볼 수 있는 링크를 호출 할 수 있는 Http Method").optional(),
                                linkWithRel("_embedded.blogs[].replies[].delete-reply").description("해당되는 블로그에 달린 리플에 대해서 삭제를 할 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.blogs[].replies[].delete-reply.type").description("해당되는 블로그에 달린 리플에 대해서 상세히 볼 수 있는 링크를 호출 할 수 있는 Http Method").optional()
                        )
                ));
    }
}