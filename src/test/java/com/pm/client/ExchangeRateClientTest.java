package com.pm.client;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;

import java.io.IOException;
import java.net.InetSocketAddress;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(MockServerExtension.class)
public class ExchangeRateClientTest {
    private final static String EXCHANGE_RESPONSE = """
                {"motd":{"msg":"If you or your company use this project or like what we doing, please consider backing us so we can continue maintaining and evolving this project.","url":"https://exchangerate.host/#/donate"},
                "success":true,"historical":true,"base":"EUR","date":"2021-01-01",
                "rates":{"AED":4.472422,"AFN":94.449943,"ALL":123.368069,"AMD":636.685742,"ANG":2.185169,"AOA":796.493364,"ARS":103.63501,
                "AUD":1.582674,"AWG":2.191647,"AZN":2.072933,"BAM":1.949636,"BBD":2.435164,"BDT":103.046945,"BGN":1.949606,"BHD":0.459042}}
            """;
    private ExchangeRateClient exchangeRateClient;
    private MockServerClient client;
    private CloseableHttpClient closeableHttpClient;

    @BeforeEach
    void setup(MockServerClient client) {
        this.client = client;
        InetSocketAddress inetSocketAddress = client.remoteAddress();
        String baseUrl = "http://" + inetSocketAddress.getHostName() + ":" + client.getPort();
        closeableHttpClient = HttpClientBuilder.create().build();
        exchangeRateClient = new ExchangeRateClient(baseUrl, closeableHttpClient);
    }

    @AfterEach
    void cleanup() throws IOException {
        closeableHttpClient.close();
    }

    @Test
    void givenWeAskForExchangeRate_clientReturnsIt() throws IOException {
        client.when(request().withMethod("GET")
                        .withPath("/2021-01-01"))
                .respond(response()
                        .withBody(EXCHANGE_RESPONSE)
                        .withStatusCode(200));

        double exchangeRate = exchangeRateClient.getExchangeRateFor("AZN");
        Assertions.assertEquals(2.072933, exchangeRate);
    }
}
