package kr.co.won.study.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.TestUser;
import kr.co.won.blog.factory.BlogFactory;
import kr.co.won.config.RestDocsConfiguration;
import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.factory.StudyFactory;
import kr.co.won.study.form.CreateStudyForm;
import kr.co.won.study.persistence.StudyPersistence;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.factory.UserFactory;
import kr.co.won.user.persistence.UserPersistence;
import org.junit.Assume;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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
@Import(value = {RestDocsConfiguration.class, UserFactory.class, BlogFactory.class, StudyFactory.class})
class StudyApiControllerTest {

    private final String HAL_JSON = "HAL JSON";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private UserPersistence userPersistence;

    @Autowired
    private StudyFactory studyFactory;

    @Autowired
    private StudyPersistence studyPersistence;


    @AfterEach
    void dataInit() {
        userPersistence.deleteAll();
        studyPersistence.deleteAll();
    }

    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "00. spring delete parameter test")
    @Test
    void springTests() throws Exception {
        List<Long> list = List.of(1L, 2L, 3L);
        Map<String, List> mapping = new HashMap<>();
        mapping.put("study", list);
        mockMvc.perform(delete("/api/studies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapping)))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @TestUser(authLevel = UserRoleType.ADMIN, email = "tester@co.kr")
    @DisplayName(value = "01. study Create Test - with ADMIN")
    @Test
    void studyCreateTests_withADMIN() throws Exception {
        CreateStudyForm form = CreateStudyForm.builder()
                .name("testingStudy")
                .shortDescription("testing study description")
                .description("dfasdfdsf")
                .allowMemberNumber(0)
                .path("/studies/testing")
                .build();
        mockMvc.perform(post("/api/studies")
                        .contentType(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.list-study").exists())
                .andExpect(jsonPath("_links.query-study").exists())
                .andExpect(jsonPath("_links.update-study").exists())
                .andExpect(jsonPath("_links.delete-study").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("create-studies",
                        /** Response Content Type */
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional(),
                                headerWithName(HttpHeaders.LOCATION).description("생성된 스터디에 대한 정보를 볼 수 있는 링크").optional()
                        ),
                        /** Request Header */
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("ACCEPT Header 값이다.").optional(),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional()
                        ),
                        /** Request Body */
                        relaxedRequestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("생성할 스터디에 대한 이름").optional(),
                                fieldWithPath("path").type(JsonFieldType.STRING).description("생성할 스터디를 볼 수 있는 링크").optional(),
                                fieldWithPath("shortDescription").type(JsonFieldType.STRING).description("생성할 스터디에 대한 짧은 설명 글").optional(),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("생성할 스터디에 대한 설명").optional(),
                                fieldWithPath("allowMemberNumber").type(JsonFieldType.NUMBER).description("모집할 인원에 대한 설정 0으로 하면, 모집할 회원의 수는 제한을 두지 않는 것이다.").optional()
                        ),
                        /** Response Body */
                        relaxedResponseFields(
                                fieldWithPath("idx").type(JsonFieldType.NUMBER).description("생성된 스터디의 고유 번호").optional(),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("생성된 스터디의 이름").optional(),
                                fieldWithPath("path").type(JsonFieldType.STRING).description("생성된 스터디의 경로").optional(),
                                fieldWithPath("shortDescription").type(JsonFieldType.STRING).description("생성된 스터디의 짧은 소개 글").optional(),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("생성한 스터디의 상태").optional(),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("스터디의 생성 시간").optional()
                        ),
                        /** Study Create Links */
                        links(
                                linkWithRel("self").description("현재 호출된 링크").optional(),
                                linkWithRel("profile").description("현재 호출된 API의 기능에 대해서 설명이 되어 있는 document를 볼 수 있는 링크이다.").optional(),
                                linkWithRel("list-study").description("스터디의 목록을 볼 수 있는 링크").optional(),
                                linkWithRel("query-study").description("생성된 스터디에 대한 정보를 볼 수 있는 링크").optional(),
                                linkWithRel("update-study").description("생성된 스터디를 업데이트할 수 있는 링크").optional(),
                                linkWithRel("delete-study").description("생성된 스터디를 삭제를 할 수 있는 링크").optional()

                        )
                ));
    }


    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "02. study List Test - with ADMIN")
    @Test
    void studyListTests_withADMIN() throws Exception {
    }

    @TestUser(authLevel = UserRoleType.ADMIN, email = "tester@co.kr")
    @DisplayName(value = "03. study find Test - with ADMIN")
    @Test
    void studyFindTests_withADMIN() throws Exception {
        String path = "path";

        UserDomain findUser = userPersistence.findByEmail("tester@co.kr").orElseThrow(() -> new IllegalArgumentException(""));
        studyFactory.makeStudy(path, "testStudy", 0, findUser);
        mockMvc.perform(get("/api/studies/{path}", path))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.update-study").exists())
                .andExpect(jsonPath("_links.delete-study").exists())
                .andDo(document("read-studies",
                        /** Response Content Type */
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional()
                        ),
                        /** Request Header */
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("ACCEPT Header 값이다.").optional(),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional()
                        ),
                        /** Request Path Parameter */
                        relaxedPathParameters(
                                parameterWithName("path").description("스터디를 볼수 잇는 경로 값이다.").optional()
                        ),
                        /** Response Body */
                        relaxedResponseFields(
                                fieldWithPath("idx").type(JsonFieldType.NUMBER).description("스터디의 고유 번호를 보여준다.").optional(),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("스터디의 이름을 보여준다.").optional(),
                                fieldWithPath("organizer").type(JsonFieldType.STRING).description("스터디를 생성한 사용자의 이메일을 보여준다.").optional(),
                                fieldWithPath("manager").type(JsonFieldType.STRING).description("스터디를 관리하는 매니저의 이메일을 보여준다.").optional(),
                                fieldWithPath("path").type(JsonFieldType.STRING).description("스터디를 볼 수 있는 스터디의 URI를 보여준다.").optional(),
                                fieldWithPath("allowMemberNumber").type(JsonFieldType.NUMBER).description("스터디를 모집을 하는 인원에 대한 수를 보여준다.").optional(),
                                fieldWithPath("joinMemberNumber").type(JsonFieldType.NUMBER).description("현재 스터디에 참여하는 인원에 대한 수를 보여준다.").optional(),
                                fieldWithPath("shortDescription").type(JsonFieldType.STRING).description("짧은 스터디에 대한 설명 글에 대해서 보여준다.").optional(),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("어떤 것에 대한 공부를 하는지 스터디에 대한 설명 글이다.").optional(),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("스터디의 현재 상태를 보여준다.\r\n 현재 스터디가 종료가 되었는지, 공개가 되었는지, 현재 인원을 모집 중인지를 나타낸다.").optional(),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("스터디가 생성된 시간").optional()
                        ),
                        /** Study Read Links */
                        links(
                                linkWithRel("self").description("현재 호출된 링크").optional(),
                                linkWithRel("profile").description("현재 호출된 API의 기능에 대해서 설명이 되어 있는 document를 볼 수 있는 링크이다.").optional(),
                                linkWithRel("list-study").description("스터디의 목록을 볼 수 있는 링크").optional(),
                                linkWithRel("query-study").description("생성된 스터디에 대한 정보를 볼 수 있는 링크").optional(),
                                linkWithRel("update-study").description("생성된 스터디를 업데이트할 수 있는 링크").optional(),
                                linkWithRel("delete-study").description("생성된 스터디를 삭제를 할 수 있는 링크").optional()

                        ))
                );
    }


}