package com.pm.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ExchangeRateClient {
    private final static String path = "2021-01-01";
    private final CloseableHttpClient httpClient;
    private final String url;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ExchangeRateClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
        this.url = "https://api.exchangerate.host";
    }

    @VisibleForTesting
    public ExchangeRateClient(String url, CloseableHttpClient httpClient) {
        this.url = url;
        this.httpClient = httpClient;
    }

    public double getExchangeRateFor(String currency) throws IOException {
        HttpUriRequest target = new HttpGet(url + "/" + path);
        CloseableHttpResponse response = this.httpClient.execute(target);
        double exchangeRate = 0.0;

        if (response.getStatusLine().getStatusCode() == 200) {
            String json = EntityUtils.toString(response.getEntity());
            ExchangeRateResponse rates = objectMapper.readValue(json, ExchangeRateResponse.class);
            exchangeRate = rates.getRate(currency);
            return exchangeRate;
        } else {
            EntityUtils.consumeQuietly(response.getEntity());
        }
        return exchangeRate;
    }
}
