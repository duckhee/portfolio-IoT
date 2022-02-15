package kr.co.won.user.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.common.Address;
import kr.co.won.auth.TestUser;
import kr.co.won.config.RestDocsConfiguration;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.factory.UserFactory;
import kr.co.won.user.form.CreateUserForm;
import kr.co.won.user.persistence.UserPersistence;
import kr.co.won.util.page.PageDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.relaxedRequestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Transactional
@Rollback
@ExtendWith(value = {MockitoExtension.class})
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(value = {RestDocsConfiguration.class, UserFactory.class})
class UserApiControllerTest {

    private final String HAL_JSON = "HAL JSON";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserPersistence userPersistence;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private ObjectMapper objectMapper;


    @AfterEach
    void userDelete() {
        userPersistence.deleteAll();
    }

    @DisplayName(value = "01. user create api Test - success")
    @Test
    void userCreateSuccessTests() throws Exception {
        String email = "test@co.kr";
        String name = "tester";
        String zipCode = "zipCode";
        String road = "road";
        String detail = "detail";
        String password = "1234";
        String confirmPassword = "1234";
        // make form data
        CreateUserForm formUser = testUserForm(email, name, zipCode, road, detail, password, confirmPassword);
        // mocking user create service layer
//        when(userService.createUser(any())).thenReturn(mockUser(formUser));
        mockMvc.perform(post("/api/users")
                        .contentType(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(formUser)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("create-users",
                        /** content type */
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional(),
                                headerWithName(HttpHeaders.LOCATION).description("생성된 사용자에 대한 정보를 볼 수 있는 링크").optional()
                        ),
                        /** request header */
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("ACCEPT Header 값이다.").optional(),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional()
                        ),
                        /** request body */
                        relaxedRequestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("회원 가입을 진행할 사용자의 이메일 주소-(필수 입력 값)").optional(),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("회원 가입을 진행할 사용자의 이름-(필수 입력 값)").optional(),
                                fieldWithPath("zipCode").type(JsonFieldType.STRING).description("회원 가입을 진행할 사용자의 우편 번호-(필수 입력 값)").optional(),
                                fieldWithPath("road").type(JsonFieldType.STRING).description("회원 가입을 진행할 사용자의 도로명 주소-(필수 입력 값)").optional(),
                                fieldWithPath("detail").type(JsonFieldType.STRING).description("회원 가입을 진행할 사용자의 상세 주소-(필수 입력 값)").optional(),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("회원 가입을 진행할 사용자의 비밀번호-(필수 입력 값)").optional(),
                                fieldWithPath("confirmPassword").type(JsonFieldType.STRING).description("회원 가입을 진행할 사용자의 확인 비밀번호-(필수 입력 값)").optional()
                        ),
                        /** response body */
                        relaxedResponseFields(
                                fieldWithPath("idx").type(JsonFieldType.NUMBER).description("회원 가입한 사용자의 고유 번호").optional(),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("회원 가입한 사용자의 이메일 주소").optional(),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("회원 가입한 사용자의 이름").optional(),
                                fieldWithPath("address").type(JsonFieldType.OBJECT).description("회원 가입한 사용자 주소를 담은 객체").optional(),
                                fieldWithPath("address.zipCode").type(JsonFieldType.STRING).description("회원 가입한 사용자의 우편 번호").optional(),
                                fieldWithPath("address.roadAddress").type(JsonFieldType.STRING).description("회원 가입한 사용자의 도로명 주소").optional(),
                                fieldWithPath("address.detailAddress").type(JsonFieldType.STRING).description("회원 가입한 사용자의 상세 주소").optional(),
                                fieldWithPath("job").type(JsonFieldType.STRING).description("회원 가입한 사용자의 직업 이름").optional(),
                                fieldWithPath("active").type(JsonFieldType.BOOLEAN).description("회원 가입한 사용자의 활성화 여부").optional(),
                                fieldWithPath("emailVerified").type(JsonFieldType.BOOLEAN).description("회원 가입한 사용자가 이메일 인증을 했는지 여부를 확인하기 위한 값").optional(),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("회원 가입한 사용자 생성된 시간").optional(),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("회원 가입한 사용자가 업데이트된 시간").optional()
                        ),
                        /** user create links */
                        links(
                                linkWithRel("self").description("현재 호출된 링크").optional(),
                                linkWithRel("profile").description("현재 호출된 API의 기능에 대해서 설명 되어 있는 document를 볼 수 있는 링크이다.").optional(),
                                linkWithRel("delete-users").description("사용자를 삭제를 할 수 있는 링크").optional(),
                                linkWithRel("query-users").description("사용자에 대한 정보를 볼 수 있는 링크").optional(),
                                linkWithRel("update-users").description("사용자에 대한 업데이트 링크").optional()
                        )
                ));
    }

    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "02. user find api Test - success")
    @Test
    void userFindSuccessTests() throws Exception {
        UserDomain testUser = userFactory.testUser("testings", "testings@co.kr", "1234");

        mockMvc.perform(get("/api/users/{idx}", testUser.getIdx())
                        .contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("_links.self").exists())
//                .andExpect(jsonPath("_links.update-users").exists())
//                .andExpect(jsonPath("_links.delete-users").exists())
                .andDo(print());

    }

    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "03. user list api Test - success")
    @Test
    void userListPagingSuccessTest() throws Exception {
        // insert test user bulk
        makeTestUserBulk("test", "1234");
        PageDto pageDto = new PageDto();
        mockMvc.perform(get("/api/users")
                        .param("page", String.valueOf(pageDto.getPage()))
                        .param("type", pageDto.getType())
                        .param("keyword", pageDto.getKeyword())
                        .param("size", String.valueOf(pageDto.getSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andDo(print())
                .andDo(document("list-users",
                        /** content type */
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional(),
                                headerWithName(HttpHeaders.LOCATION).description("생성된 사용자에 대한 정보를 볼 수 있는 링크").optional()
                        ),
                        /** request header */
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("ACCEPT Header 값이다.").optional(),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional()
                        ),
                        /** request parameter */
                        relaxedRequestParameters(
                                parameterWithName("page").description("페이지 번호").optional(),
                                parameterWithName("size").description("한 페이지에서 몇개를 가져올 것인지 크기 설정").optional(),
                                parameterWithName("type").description("검색할 category 입력 (email, name)").optional(),
                                parameterWithName("keyword").description("검색할 문자열").optional()
                        ),
                        /** response body */
                        relaxedResponseFields(
                                fieldWithPath("_embedded.users").type(JsonFieldType.ARRAY).description("사용자에 대한 정보를 담고 있는 배열").optional(),
                                fieldWithPath("_embedded.users[].idx").type(JsonFieldType.NUMBER).description("회원 가입한 사용자에 대한 고유 번호").optional(),
                                fieldWithPath("_embedded.users[].email").type(JsonFieldType.STRING).description("회원 가입한 사용자의 이메일 주소").optional(),
                                fieldWithPath("_embedded.users[].name").type(JsonFieldType.STRING).description("회원 가입한 사용자의 이름").optional(),
                                fieldWithPath("_embedded.users[].address").type(JsonFieldType.OBJECT).description("회원 가입한 사용자 주소를 담은 객체").optional(),
                                fieldWithPath("_embedded.users[].address.zipCode").type(JsonFieldType.STRING).description("회원 가입한 사용자의 우편 번호").optional(),
                                fieldWithPath("_embedded.users[].address.roadAddress").type(JsonFieldType.STRING).description("회원 가입한 사용자의 도로명 주소").optional(),
                                fieldWithPath("_embedded.users[].address.detailAddress").type(JsonFieldType.STRING).description("회원 가입한 사용자의 상세 주소").optional(),
                                fieldWithPath("_embedded.users[].job").type(JsonFieldType.STRING).description("회원 가입한 사용자의 직업 이름").optional(),
                                fieldWithPath("_embedded.users[].active").type(JsonFieldType.BOOLEAN).description("회원 가입한 사용자의 활성화 여부").optional(),
                                fieldWithPath("_embedded.users[].emailVerified").type(JsonFieldType.BOOLEAN).description("회원 가입한 사용자가 이메일 인증을 했는지 여부를 확인하기 위한 값").optional(),
                                fieldWithPath("_embedded.users[].roles").type(JsonFieldType.ARRAY).description("회원 가입한 사용자가 가지고 있는 권한").optional(),
                                fieldWithPath("_embedded.users[].createdAt").type(JsonFieldType.STRING).description("회원 가입한 사용자 생성된 시간").optional(),
                                fieldWithPath("_embedded.users[].updatedAt").type(JsonFieldType.STRING).description("회원 가입한 사용자가 업데이트된 시간").optional(),
                                fieldWithPath("page").type(JsonFieldType.OBJECT).description("페이지에 대한 정보를 담고 있는 객체").optional(),
                                fieldWithPath("page.size").type(JsonFieldType.NUMBER).description("한 페이지에서 보여줄 회원의 숫자").optional(),
                                fieldWithPath("page.totalElements").type(JsonFieldType.NUMBER).description("회원의 전체 수").optional(),
                                fieldWithPath("page.totalPages").type(JsonFieldType.NUMBER).description("회원 목록이 가지고 있는 전체 페이지 수").optional(),
                                fieldWithPath("page.number").type(JsonFieldType.NUMBER).description("현재 보고 있는 페이지의 번호").optional()
                        ),
                        /** user create links */
                        relaxedLinks(
                                linkWithRel("self").description("현재 호출된 링크").optional(),
                                linkWithRel("profile").description("현재 호출된 API의 기능에 대한 설명 되어 있는 document를 볼 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.users[].self").description("해당되는 사용자에 대해서 상세히 볼 수 있는 링크이다.").optional(),
//                                linkWithRel("_embedded.users[0].query-users").description("해당되는 사용자에 대해서 상세히 볼 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.users[].update-users").description("해당되는 사용자에 대해서 수정을 할 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.users[].update-users.type").description("해당되는 사용자에 대해서 상세히 볼 수 있는 링크를 호출 할 수 있는 Http Method").optional(),
                                linkWithRel("_embedded.users[].delete-users").description("해당되는 사용자에 대해서 삭제를 할 수 있는 링크이다.").optional(),
                                linkWithRel("_embedded.users[].delete-users.type").description("해당되는 사용자에 대해서 상세히 볼 수 있는 링크를 호출 할 수 있는 Http Method").optional()
                        )
                ));
    }

    private CreateUserForm testUserForm(String email, String name, String zipCode, String road, String detail, String password, String confirmPassword) {
        return CreateUserForm.builder()
                .email(email)
                .name(name)
                .zipCode(zipCode)
                .roadAddress(road)
                .detailAddress(detail)
                .password(password)
                .confirmPassword(confirmPassword)
                .build();
    }

    // mocking user
    private UserDomain mockUser(CreateUserForm form) {
        Address testAddress = new Address(form.getZipCode(), form.getRoadAddress(), form.getDetailAddress());
        UserDomain testUser = UserDomain.builder()
                .idx(1L)
                .name(form.getName())
                .email(form.getEmail())
                .password(form.getPassword())
                .address(testAddress)
                .build();
        UserRoleDomain testRole = UserRoleDomain.builder()
                .idx(1L)
                .role(UserRoleType.USER)
                .build();
        testUser.addRole(testRole);
        return testUser;
    }

    private UserDomain makeTestUser() {
        return null;
    }

    protected List<UserDomain> makeTestUserBulk(String name, String password) {
        List<UserDomain> userList = new ArrayList<>();
        Address testAddress = new Address("zipCode", "roadAddress", "detailAddress");
        for (int i = 0; i < 100; i++) {
            UserDomain newUser = UserDomain.builder()
                    .name(name + i)
                    .email(name + i + "@co.kr")
                    .password(passwordEncoder.encode(password))
                    .address(testAddress)
                    .build();
            UserRoleDomain defaultRole = UserRoleDomain.builder()
                    .role(UserRoleType.USER)
                    .build();
            newUser.addRole(defaultRole);
            userList.add(newUser);
        }
        List<UserDomain> savedAllUser = userPersistence.saveAll(userList);
        return savedAllUser;
    }
}