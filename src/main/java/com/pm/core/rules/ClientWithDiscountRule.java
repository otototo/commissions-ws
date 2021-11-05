package com.pm.core.rules;

import com.pm.api.TransactionRequest;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Transaction price for the client with ID of `42` is `0.05€` (*unless other rules set lower commission*).
 */
public class ClientWithDiscountRule implements CommissionRule {
    private final BigDecimal discountedCommission;
    private final Set<Integer> clients;

    public ClientWithDiscountRule(BigDecimal discountedCommission, Set<Integer> clients) {
        this.discountedCommission = discountedCommission;
        this.clients = clients;
    }

    @Override
    public boolean shouldApply(TransactionRequest params) {
        return clients.contains(params.getClientId());
    }

    @Override
    public BigDecimal calculateCommission(TransactionRequest params) {
        return discountedCommission;
    }
}
