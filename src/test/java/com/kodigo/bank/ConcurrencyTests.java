package com.kodigo.bank;

import com.kodigo.bank.exception.BusinessException;
import com.kodigo.bank.service.AccountService;
import com.kodigo.bank.service.AtmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

// Varias operaciones al mismo tiempo sobre la misma cuenta no deben gastar el mismo saldo
@SpringBootTest
class ConcurrencyTests {
    @Autowired
    private TestData testData;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AtmService atmService;

    @BeforeEach
    void setUp() {
        testData.reset();
    }

    private int runConcurrently(int times, Runnable operation) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(times);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger succeeded = new AtomicInteger();
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            futures.add(executor.submit(() -> {
                start.await();
                try {
                    operation.run();
                    succeeded.incrementAndGet();
                } catch (BusinessException e) {
                    // rechazada por fondos insuficientes: es lo esperado
                }
                return null;
            }));
        }
        start.countDown();
        for (Future<?> future : futures) {
            future.get(30, TimeUnit.SECONDS);
        }
        executor.shutdown();
        return succeeded.get();
    }

    @Test
    void concurrentTransfersNeverOverdraw() throws Exception {
        // $1000 alcanzan para 3 transferencias de $300, no para 10
        int succeeded = runConcurrently(10,
                () -> accountService.transfer("gustavo", "1000-0002", new BigDecimal("300"), null));

        assertThat(succeeded).isEqualTo(3);
        assertThat(testData.balance("1000-0001")).isEqualByComparingTo("100.00");
        assertThat(testData.balance("1000-0002")).isEqualByComparingTo("1400.00");
    }

    @Test
    void concurrentDepositsAreNotLost() throws Exception {
        int succeeded = runConcurrently(10,
                () -> atmService.deposit(TestData.ATM_ID, "1000-0002", new BigDecimal("10")));

        assertThat(succeeded).isEqualTo(10);
        assertThat(testData.balance("1000-0002")).isEqualByComparingTo("600.00");
    }
}
