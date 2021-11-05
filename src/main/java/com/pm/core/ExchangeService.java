package com.pm.core;

import com.pm.client.ExchangeRateClient;

import java.io.IOException;
import java.math.BigDecimal;

public class ExchangeService {
    private final ExchangeRateClient client;

    public ExchangeService(ExchangeRateClient client) {
        this.client = client;
    }

    double getExchangeRateFor(String currency) throws IOException {
        return client.getExchangeRateFor(currency);
    }

    public BigDecimal exchangeToEUR(BigDecimal amount, String currency) throws IOException {
        BigDecimal result = amount;
        if (!currency.equalsIgnoreCase("EUR")) {
            double exchangeRate = getExchangeRateFor(currency);
            result = amount.multiply(BigDecimal.valueOf(exchangeRate));
        }
        return result;
    }
}
