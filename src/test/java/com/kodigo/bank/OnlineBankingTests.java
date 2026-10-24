package com.kodigo.bank;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OnlineBankingTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestData testData;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        testData.reset();
    }

    private MockHttpServletRequestBuilder as(String username, MockHttpServletRequestBuilder request) {
        return request.with(user(username).roles("CUSTOMER")).with(csrf()).contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void summaryShowsOwnAccount() throws Exception {
        mockMvc.perform(as("gustavo", get("/api/v1/account")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("1000-0001"))
                .andExpect(jsonPath("$.holderName").value("Gustavo Montoya"))
                .andExpect(jsonPath("$.available").value(1000.00));
    }

    @Test
    void transferMovesMoneyAndRecordsBothSides() throws Exception {
        mockMvc.perform(as("gustavo", post("/api/v1/account/transfers"))
                        .content("{\"toAccountNumber\":\"1000-0002\",\"amount\":250.50,\"description\":\"Dinner\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("TRANSFER_OUT"))
                .andExpect(jsonPath("$.balanceAfter").value(749.50));

        assertThat(testData.balance("1000-0001")).isEqualByComparingTo("749.50");
        assertThat(testData.balance("1000-0002")).isEqualByComparingTo("750.50");
        mockMvc.perform(as("maria", get("/api/v1/account/transactions")))
                .andExpect(jsonPath("$[0].type").value("TRANSFER_IN"))
                .andExpect(jsonPath("$[0].description").value("Transfer from 1000-0001 - Dinner"));
    }

    @Test
    void transferRejectsInsufficientFunds() throws Exception {
        mockMvc.perform(as("gustavo", post("/api/v1/account/transfers"))
                        .content("{\"toAccountNumber\":\"1000-0002\",\"amount\":5000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Insufficient funds. Available balance: $1000.00"));
        assertThat(testData.balance("1000-0001")).isEqualByComparingTo("1000.00");
    }

    @Test
    void transferRejectsInvalidRequests() throws Exception {
        mockMvc.perform(as("gustavo", post("/api/v1/account/transfers"))
                        .content("{\"toAccountNumber\":\"1000-0001\",\"amount\":10}"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(as("gustavo", post("/api/v1/account/transfers"))
                        .content("{\"toAccountNumber\":\"9999-9999\",\"amount\":10}"))
                .andExpect(status().isNotFound());
        mockMvc.perform(as("gustavo", post("/api/v1/account/transfers"))
                        .content("{\"toAccountNumber\":\"1000-0002\",\"amount\":-10}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void withdrawalCodeReservesMoneyWithoutDebitingIt() throws Exception {
        String body = mockMvc.perform(as("gustavo", post("/api/v1/account/withdrawal-codes")).content("{\"amount\":300}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(org.hamcrest.Matchers.matchesPattern("\\d{8}")))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(as("gustavo", get("/api/v1/account")))
                .andExpect(jsonPath("$.balance").value(1000.00))
                .andExpect(jsonPath("$.reserved").value(300.00))
                .andExpect(jsonPath("$.available").value(700.00));

        // El codigo completo no se vuelve a mostrar, solo los ultimos 2 digitos
        String code = objectMapper.readTree(body).get("code").asText();
        mockMvc.perform(as("gustavo", get("/api/v1/account/withdrawal-codes")))
                .andExpect(jsonPath("$[0].codeHint").value(code.substring(6)))
                .andExpect(jsonPath("$[0].codeHash").doesNotExist())
                .andExpect(jsonPath("$[0].displayStatus").value("ACTIVE"));

        // Lo reservado ya no se puede transferir
        mockMvc.perform(as("gustavo", post("/api/v1/account/transfers"))
                        .content("{\"toAccountNumber\":\"1000-0002\",\"amount\":800}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void withdrawalCodeValidatesAmount() throws Exception {
        mockMvc.perform(as("gustavo", post("/api/v1/account/withdrawal-codes")).content("{\"amount\":25}"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(as("gustavo", post("/api/v1/account/withdrawal-codes")).content("{\"amount\":2000}"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(as("maria", post("/api/v1/account/withdrawal-codes")).content("{\"amount\":600}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(org.hamcrest.Matchers.startsWith("Insufficient funds")));
    }

    @Test
    void cancellingCodeReleasesReservation() throws Exception {
        String body = mockMvc.perform(as("gustavo", post("/api/v1/account/withdrawal-codes")).content("{\"amount\":100}"))
                .andReturn().getResponse().getContentAsString();
        JsonNode created = objectMapper.readTree(body);

        // Otro cliente no puede cancelar un codigo ajeno
        mockMvc.perform(as("maria", delete("/api/v1/account/withdrawal-codes/" + created.get("id").asLong())))
                .andExpect(status().isNotFound());

        mockMvc.perform(as("gustavo", delete("/api/v1/account/withdrawal-codes/" + created.get("id").asLong())))
                .andExpect(status().isNoContent());
        mockMvc.perform(as("gustavo", get("/api/v1/account")))
                .andExpect(jsonPath("$.available").value(1000.00));
    }
}
