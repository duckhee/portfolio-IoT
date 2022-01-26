package kr.co.won.user.api;

import kr.co.won.address.Address;
import kr.co.won.auth.TestUser;
import kr.co.won.config.AppConfiguration;
import kr.co.won.config.RestDocsConfiguration;
import kr.co.won.config.SecurityConfiguration;
import kr.co.won.config.datasources.DataSourceConfiguration;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.form.CreateUserForm;
import kr.co.won.user.persistence.UserPersistence;
import kr.co.won.user.service.UserService;
import kr.co.won.user.validation.CreateUserValidation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(value = {MockitoExtension.class})
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(value = {RestDocsConfiguration.class})
class UserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private ObjectMapper objectMapper = new ObjectMapper();


    @DisplayName(value = "01. user create api test - success")
    @Test
    void userCreateSuccessTests() throws Exception {
        String email = "test@co.kr";
        String name = "name";
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
        ;
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
                .role(UserRoleType.USER)
                .build();
        testUser.addRole(testRole);
        return testUser;
    }

}