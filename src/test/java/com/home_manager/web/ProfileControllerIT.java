package com.home_manager.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ProfileControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(value = "b_davidov@abv.bg", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testProfilePage() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/profile")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.view().name("profile"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails(value = "b_davidov@abv.bg", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testCashier() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/cashier")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.view().name("cashier"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails(value = "b_davidov@abv.bg", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testRegisterCashier() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/profile/register-cashier")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.view().name("register"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails(value = "b_davidov@abv.bg", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testAssignCashier() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/profile/assign")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.view().name("manager/assign"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }



}
