package com.pm.core;

import com.pm.client.ExchangeRateClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExchangeServiceTest {
    private ExchangeRateClient client = mock(ExchangeRateClient.class);
    private ExchangeService exchangeService;
    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        exchangeService = new ExchangeService(client);
    }

    @AfterEach
    void cleanup() throws Exception {
        closeable.close();
    }

    @Test
    void calculateExchangeCorrectly() throws IOException {
        when(client.getExchangeRateFor("USD")).thenReturn(1.5);
        when(client.getExchangeRateFor("GBP")).thenReturn(0.0);
        BigDecimal amount1 = exchangeService.exchangeToEUR(BigDecimal.ONE, "USD");
        BigDecimal amount2 = exchangeService.exchangeToEUR(BigDecimal.ONE, "GBP");
        Assertions.assertEquals(BigDecimal.valueOf(1.5), amount1);
        Assertions.assertEquals(BigDecimal.valueOf(0).setScale(1), amount2);
    }

}