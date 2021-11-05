package com.pm.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateResponse {
    private final HashMap<String, Double> rates;

    @JsonCreator
    public ExchangeRateResponse(@JsonProperty("rates") HashMap<String, Double> rates) {
        this.rates = rates;
    }

    @JsonIgnore
    public Double getRate(String currency) {
        return rates.get(currency);
    }
}
