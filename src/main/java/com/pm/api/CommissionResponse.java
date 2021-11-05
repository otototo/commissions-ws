package com.pm.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CommissionResponse {

    @JsonProperty("amount")
    private final String amount;
    @JsonProperty("currency")
    private final String currency;


    @JsonCreator
    public CommissionResponse(@JsonProperty("amount") String amount,
                              @JsonProperty("currency") String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommissionResponse that = (CommissionResponse) o;
        return Objects.equals(amount, that.amount) && Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

}
