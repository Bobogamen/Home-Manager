package com.home_manager.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(value = "b_davidov@abv.bg", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testViewHome() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/profile/homesGroup1/home1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.view().name("manager/home"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails(value = "b_davidov@abv.bg", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testAddFee() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/profile/homesGroup1/add-fee")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.view().name("manager/add-fee"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
