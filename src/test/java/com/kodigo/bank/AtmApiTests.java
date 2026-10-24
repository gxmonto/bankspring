package com.kodigo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodigo.bank.repository.WithdrawalCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AtmApiTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestData testData;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WithdrawalCodeRepository withdrawalCodeRepository;

    @BeforeEach
    void setUp() {
        testData.reset();
    }

    private MockHttpServletRequestBuilder atm(MockHttpServletRequestBuilder request) {
        return request.header("X-ATM-ID", TestData.ATM_ID).header("X-ATM-KEY", TestData.ATM_KEY)
                .contentType(MediaType.APPLICATION_JSON);
    }

    private String newCode(String amount) throws Exception {
        String body = mockMvc.perform(post("/api/v1/account/withdrawal-codes")
                        .with(user("gustavo").roles("CUSTOMER")).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content("{\"amount\":" + amount + "}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(body).get("code").asText();
    }

    private String withdrawal(String code, String amount) {
        return "{\"code\":\"" + code + "\",\"amount\":" + amount + "}";
    }

    @Test
    void atmNeedsValidCredentials() throws Exception {
        mockMvc.perform(get("/api/atm/v1/accounts/1000-0001")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/atm/v1/accounts/1000-0001").header("X-ATM-ID", TestData.ATM_ID).header("X-ATM-KEY", "wrong"))
                .andExpect(status().isUnauthorized());
        // Un cliente con sesion tampoco puede usar la API de cajeros
        mockMvc.perform(get("/api/atm/v1/accounts/1000-0001").with(user("gustavo").roles("CUSTOMER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void redeemingCodeDispensesCashOnce() throws Exception {
        String code = newCode("200");

        mockMvc.perform(atm(post("/api/atm/v1/withdrawals")).content(withdrawal(code, "200")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("WITHDRAWAL"))
                .andExpect(jsonPath("$.amount").value(200))
                .andExpect(jsonPath("$.atmCode").value(TestData.ATM_ID))
                .andExpect(jsonPath("$.holderName").value("Gus**** Mon****"));
        assertThat(testData.balance("1000-0001")).isEqualByComparingTo("800.00");

        // El mismo codigo no sirve dos veces
        mockMvc.perform(atm(post("/api/atm/v1/withdrawals")).content(withdrawal(code, "200")))
                .andExpect(status().isBadRequest());
        assertThat(testData.balance("1000-0001")).isEqualByComparingTo("800.00");
    }

    @Test
    void redeemingWithWrongAmountOrCodeFails() throws Exception {
        String code = newCode("100");
        mockMvc.perform(atm(post("/api/atm/v1/withdrawals")).content(withdrawal(code, "500")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid or expired code, or the amount does not match"));
        mockMvc.perform(atm(post("/api/atm/v1/withdrawals")).content(withdrawal("12345678", "100")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid or expired code, or the amount does not match"));
        assertThat(testData.balance("1000-0001")).isEqualByComparingTo("1000.00");
    }

    @Test
    void expiredCodeCannotBeRedeemed() throws Exception {
        String code = newCode("100");
        withdrawalCodeRepository.findAll().forEach(w -> {
            w.setExpiresAt(LocalDateTime.now().minusMinutes(1));
            withdrawalCodeRepository.save(w);
        });

        mockMvc.perform(atm(post("/api/atm/v1/withdrawals")).content(withdrawal(code, "100")))
                .andExpect(status().isBadRequest());
        // Al vencer, el dinero reservado vuelve a estar disponible
        mockMvc.perform(get("/api/v1/account").with(user("gustavo").roles("CUSTOMER")))
                .andExpect(jsonPath("$.available").value(1000.00));
    }

    @Test
    void cashDepositCreditsAccount() throws Exception {
        mockMvc.perform(atm(get("/api/atm/v1/accounts/1000-0002")))
                .andExpect(jsonPath("$.holderName").value("Mar** Lop**"));

        mockMvc.perform(atm(post("/api/atm/v1/deposits")).content("{\"accountNumber\":\"1000-0002\",\"amount\":150}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("DEPOSIT"))
                .andExpect(jsonPath("$.reference").value(org.hamcrest.Matchers.startsWith("TX-")));
        assertThat(testData.balance("1000-0002")).isEqualByComparingTo("650.00");

        mockMvc.perform(atm(post("/api/atm/v1/deposits")).content("{\"accountNumber\":\"9999-9999\",\"amount\":150}"))
                .andExpect(status().isNotFound());
    }
}
