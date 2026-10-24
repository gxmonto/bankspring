package com.kodigo.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestData testData;

    @BeforeEach
    void setUp() {
        testData.reset();
    }

    @Test
    void loginChecksBcryptPassword() throws Exception {
        mockMvc.perform(formLogin().user("gustavo").password("secret"))
                .andExpect(authenticated().withUsername("gustavo").withRoles("CUSTOMER"))
                .andExpect(redirectedUrl("/index"));
        mockMvc.perform(formLogin().user("gustavo").password("wrong"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void adminGetsAdminRole() throws Exception {
        mockMvc.perform(formLogin().user("admin").password("secret"))
                .andExpect(authenticated().withRoles("ADMIN"));
    }

    @Test
    void apiRequiresLogin() throws Exception {
        mockMvc.perform(get("/api/v1/account")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/v1/customers")).andExpect(status().isUnauthorized());
    }

    @Test
    void pagesRedirectToLogin() throws Exception {
        mockMvc.perform(get("/index").accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void customersCannotUseAdminApi() throws Exception {
        mockMvc.perform(get("/api/v1/customers").with(user("gustavo").roles("CUSTOMER")))
                .andExpect(status().isForbidden());
        // Antes cualquiera podia cambiar un saldo con este POST
        mockMvc.perform(post("/api/v1/customers").with(user("gustavo").roles("CUSTOMER")).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content("{\"customerId\":1,\"balance\":999999}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanListCustomersWithoutPasswords() throws Exception {
        mockMvc.perform(get("/api/v1/customers").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").exists())
                .andExpect(jsonPath("$[0].people.password").doesNotExist());
    }

    @Test
    void postWithoutCsrfTokenIsRejected() throws Exception {
        mockMvc.perform(post("/api/v1/account/transfers").with(user("gustavo").roles("CUSTOMER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"toAccountNumber\":\"1000-0002\",\"amount\":10}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void publicPagesAreReachable() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk());
        mockMvc.perform(get("/atm")).andExpect(status().isOk());
    }
}
