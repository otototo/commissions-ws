package com.pm.resources;

import com.pm.api.CommissionResponse;
import com.pm.api.TransactionRequest;
import com.pm.core.CommissionService;
import com.pm.core.ExchangeService;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(DropwizardExtensionsSupport.class)
public class ResourceTest {
    private static final String TRANSACTION_INPUT_EUR_1 = """
            {
                  "date": "2021-01-01",
                  "amount": "100.00",
                  "currency": "EUR",
                  "client_id": 42
                  }""";
    private static final String TRANSACTION_INPUT_USD_1 = """
            {
                 "date": "2021-01-01",
                 "amount": "200.40",
                 "currency": "USD",
                 "client_id": 42
                 }""";

    private static final ExchangeService EXCHANGE_SERVICE = mock(ExchangeService.class);
    private static final CommissionService COMMISSION_SERVICE = mock(CommissionService.class);
    private static final ResourceExtension TRANSACTION_RESOURCE = ResourceExtension.builder()
            .addResource(new TransactionResource(EXCHANGE_SERVICE, COMMISSION_SERVICE))
            .build();//todo - change to mock

    @Test
    void givenClient42MakesARequestWithEUR_respondWithCommission() throws IOException {
        when(EXCHANGE_SERVICE.exchangeToEUR(any(BigDecimal.class), anyString()))
                .thenReturn(BigDecimal.ONE);
        when(COMMISSION_SERVICE.calculateCommission(any(TransactionRequest.class)))
                .thenReturn(BigDecimal.valueOf(0.05));

        Response response = TRANSACTION_RESOURCE.target("/commissions").request()
                .post(Entity.entity(TRANSACTION_INPUT_EUR_1, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(200, response.getStatus());

        CommissionResponse actual = response.readEntity(CommissionResponse.class);
        CommissionResponse expected = new CommissionResponse("0.05", "EUR");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenClient42MakesARequestWithUSD_respondWithCommission() throws IOException {
        when(EXCHANGE_SERVICE.exchangeToEUR(any(BigDecimal.class), anyString()))
                .thenReturn(BigDecimal.ONE);
        when(COMMISSION_SERVICE.calculateCommission(any(TransactionRequest.class)))
                .thenReturn(BigDecimal.valueOf(0.05));

        Response response = TRANSACTION_RESOURCE.target("/commissions").request()
                .post(Entity.entity(TRANSACTION_INPUT_USD_1, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(200, response.getStatus());

        CommissionResponse actual = response.readEntity(CommissionResponse.class);
        CommissionResponse expected = new CommissionResponse("0.05", "EUR");
        Assertions.assertEquals(expected, actual);
    }
}
