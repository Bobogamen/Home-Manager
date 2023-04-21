package com.home_manager.web;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ForgotAndResetPasswordControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testForgotPassword() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/forgot-password")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.view().name("forgot_password"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
