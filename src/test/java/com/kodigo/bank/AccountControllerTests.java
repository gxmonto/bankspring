package com.kodigo.bank;

import com.kodigo.bank.entity.Customer;
import com.kodigo.bank.entity.People;
import com.kodigo.bank.repository.CustomerRepository;
import com.kodigo.bank.repository.PeopleRepository;
import com.kodigo.bank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PeopleRepository peopleRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    private Long customerId;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        customerRepository.deleteAll();
        peopleRepository.deleteAll();

        People people = new People();
        people.setName("Gustavo");
        people.setUsername("admin");
        people.setPassword("123");
        peopleRepository.save(people);

        Customer customer = new Customer();
        customer.setBalance(new BigDecimal("1000.00"));
        customer.setPeople(people);
        customerId = customerRepository.save(customer).getCustomerId();
    }

    private MockHttpSession login() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/login").session(session).param("username", "admin").param("password", "123"))
                .andExpect(redirectedUrl("/index"));
        return session;
    }

    private BigDecimal balanceInDb() {
        return customerRepository.findById(customerId).orElseThrow().getBalance();
    }

    @Test
    void depositIsSavedInDatabase() throws Exception {
        MockHttpSession session = login();

        mockMvc.perform(post("/api/v1/account/deposit").session(session)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"amount\": 250.50}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1250.50));

        assertThat(balanceInDb()).isEqualByComparingTo("1250.50");
        assertThat(transactionRepository.findByCustomerCustomerIdOrderByDateDesc(customerId)).hasSize(1);
    }

    @Test
    void withdrawIsSavedInDatabase() throws Exception {
        MockHttpSession session = login();

        mockMvc.perform(post("/api/v1/account/withdraw").session(session)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"amount\": 300}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(700.00));

        assertThat(balanceInDb()).isEqualByComparingTo("700.00");
        mockMvc.perform(get("/api/v1/account/transactions").session(session))
                .andExpect(jsonPath("$[0].type").value("WITHDRAWAL"));
    }

    @Test
    void withdrawWithInsufficientFundsIsRejected() throws Exception {
        MockHttpSession session = login();

        mockMvc.perform(post("/api/v1/account/withdraw").session(session)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"amount\": 5000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());

        assertThat(balanceInDb()).isEqualByComparingTo("1000.00");
    }

    @Test
    void negativeAmountIsRejected() throws Exception {
        MockHttpSession session = login();

        mockMvc.perform(post("/api/v1/account/deposit").session(session)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"amount\": -10}"))
                .andExpect(status().isBadRequest());

        assertThat(balanceInDb()).isEqualByComparingTo("1000.00");
    }

    @Test
    void requiresLogin() throws Exception {
        mockMvc.perform(get("/api/v1/account/balance")).andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/v1/account/deposit")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"amount\": 10}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void wrongPasswordShowsLoginAgain() throws Exception {
        mockMvc.perform(post("/login").param("username", "admin").param("password", "bad"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"));
    }
}
