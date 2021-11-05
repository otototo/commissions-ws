package com.pm.core.rules;

import com.pm.api.TransactionRequest;
import com.pm.db.TransactionStore;

import java.math.BigDecimal;

/**
 * Client after reaching transaction turnover of `1000.00€` (per month) gets a discount and
 * transaction commission is `0.03€` for the following transactions.
 */
public class HighTurnoverDiscountRule implements CommissionRule {
    private final BigDecimal reqCommissionSum;
    private final BigDecimal commission;

    private final TransactionStore transactionStore;

    public HighTurnoverDiscountRule(TransactionStore store, BigDecimal reqCommissionSum, BigDecimal commission) {
        this.transactionStore = store;
        this.reqCommissionSum = reqCommissionSum;
        this.commission = commission;
    }

    @Override
    public boolean shouldApply(TransactionRequest params) {
        BigDecimal turnover = transactionStore.getMonthlyTurnover(params.getClientId(), params.getDate());
        return turnover.compareTo(reqCommissionSum) >= 0;
    }

    @Override
    public BigDecimal calculateCommission(TransactionRequest params) {
        return commission;
    }
}
