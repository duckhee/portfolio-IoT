package kr.co.won.user.controller;

import kr.co.won.user.factory.UserFactory;
import kr.co.won.user.form.CreateUserForm;
import kr.co.won.user.persistence.UserPersistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(value = {UserFactory.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private UserPersistence userPersistence;

    @AfterEach
    void testDataSet() {
        userPersistence.deleteAll();
    }

    @DisplayName(value = "01. Registe User Page Test - with GET")
    @Test
    void userCreatePageTests() throws Exception {
        mockMvc.perform(get("/users/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/createPage"))
                .andDo(print());
    }

    @DisplayName(value = "01. Registe User Do Test - with POST(SUCCESS)")
    @Test
    void userCreateDoSuccessTests() throws Exception {
        String email = "testing@co.kr";
        String name = "tester";
        String password = "1234";
        String zipCode = "zipCode";
        String roadAddress = "roadAddress";
        String detailAddress = "detailAddress";

        mockMvc.perform(post("/users/create")
                        .with(csrf())
                        .param("email", email)
                        .param("name", name)
                        .param("password", password)
                        .param("confirmPassword", password)
                        .param("zipCode", zipCode)
                        .param("roadAddress", roadAddress)
                        .param("detailAddress", detailAddress)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"))
                .andExpect(redirectedUrl("/login"));
    }

}