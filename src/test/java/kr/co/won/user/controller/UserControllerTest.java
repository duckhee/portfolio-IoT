package kr.co.won.user.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName(value = "01. Registe User Page Test - with GET")
    @Test
    void userCreatePageTests() throws Exception {
        mockMvc.perform(get("/users/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/createPage"))
                .andDo(print());
    }

    @DisplayName(value = "test")
    @Test
    void denyTest() throws Exception{
        mockMvc.perform(get("/users/profile"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
}