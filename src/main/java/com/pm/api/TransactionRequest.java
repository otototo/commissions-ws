package com.pm.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionRequest {
    @JsonProperty("date")
    private final LocalDate date;
    @JsonProperty("amount")
    private final BigDecimal amount;
    @JsonProperty("currency")
    private final String currency;
    @JsonProperty("client_id")
    private final int clientId;

    @JsonCreator
    public TransactionRequest(@JsonProperty("date") LocalDate date,
                              @JsonProperty("amount") BigDecimal amount,
                              @JsonProperty("currency") String currency,
                              @JsonProperty("client_id") int clientId) {
        this.date = date;
        this.amount = amount;
        this.currency = currency;
        this.clientId = clientId;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public int getClientId() {
        return clientId;
    }
}
