package kr.co.won.user.api;

import kr.co.won.address.Address;
import kr.co.won.config.RestDocsConfiguration;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.form.CreateUserForm;
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
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

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
@ExtendWith(value = {MockitoExtension.class})
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(value = {RestDocsConfiguration.class})
class UserApiControllerTest {

    private final String HAL_JSON = "HAL JSON";

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();


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
                        /** request header*/
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
                                linkWithRel("delete-users").description("사용자를 삭제를 할 수 있는 링크").optional(),
                                linkWithRel("query-users").description("사용자에 대한 정보를 볼 수 있는 링크").optional(),
                                linkWithRel("update-users").description("사용자에 대한 업데이트 링크").optional()
                        )
                ));
    }

    @DisplayName(value = "02. user find api Test - success")
    @Test
    void userFindSuccessTests() throws Exception {
        mockMvc.perform(get("/api/users/{idx}", 1L)
                .contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists());
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

}