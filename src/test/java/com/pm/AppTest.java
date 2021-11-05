package com.pm;

import com.pm.api.CommissionResponse;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;

@ExtendWith(DropwizardExtensionsSupport.class)
class AppTest {

    private static final String TEMPLATE = """
            {
                 "date": "%s",
                 "amount": "%f",
                 "currency": "EUR",
                 "client_id": %d
            }""";

    private static final DropwizardAppExtension<AppConfiguration> EXT = new DropwizardAppExtension<>(App.class);
    private Client client;

    @BeforeEach
    void setup() {
        client = EXT.client();
    }

    @Test
    void acceptanceIntegrationTest() {
        Assertions.assertEquals("0.05", postRequest(toParams(42, 2000.0, "2021-01-02")));
        Assertions.assertEquals("2.50", postRequest(toParams(1, 500.0, "2021-01-03")));
        Assertions.assertEquals("2.50", postRequest(toParams(1, 499.0, "2021-01-04")));
        Assertions.assertEquals("0.50", postRequest(toParams(1, 100.0, "2021-01-05")));
        Assertions.assertEquals("0.03", postRequest(toParams(1, 1.0, "2021-01-06")));
        Assertions.assertEquals("2.50", postRequest(toParams(1, 500.0, "2021-02-01")));
    }

    private String postRequest(String entity) {
        return client.target(String.format("http://localhost:%d/commissions", EXT.getLocalPort()))
                .request()
                .post(Entity.json(entity))
                .readEntity(CommissionResponse.class)
                .getAmount();
    }

    private String toParams(int clientId, double amount, String date) {
        return String.format(TEMPLATE, date, amount, clientId);
    }
}