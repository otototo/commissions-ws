package com.pm.resources;

import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.pm.api.CommissionResponse;
import com.pm.api.TransactionRequest;
import com.pm.core.CommissionService;
import com.pm.core.ExchangeService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.math.BigDecimal;

@Path("/")
public class TransactionResource {
    private final ExchangeService exchangeService;
    private final CommissionService commissionService;

    public TransactionResource(ExchangeService exchangeService, CommissionService commissionService) {
        this.exchangeService = exchangeService;
        this.commissionService = commissionService;
    }

    @POST
    @Path("/commissions")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(name = "get-requests-timed")
    @Metered(name = "get-requests-metered")
    public CommissionResponse getCommissionResponse(TransactionRequest transactionRequest) throws IOException {
        BigDecimal commission = BigDecimal.ZERO;
//        try {
        BigDecimal amountInEUR = exchangeService.exchangeToEUR(transactionRequest.getAmount(),
                transactionRequest.getCurrency());
        TransactionRequest params = new TransactionRequest(transactionRequest.getDate(), amountInEUR, "EUR", transactionRequest.getClientId());

        commission = commissionService.calculateCommission(params);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//            //todo add logging + error handler for resources
//        }
        return new CommissionResponse(commission.toString(), "EUR");
    }
}
