package com.pm.core.rules;

import com.pm.api.TransactionRequest;

import java.math.BigDecimal;

/**
 * By default, the price for every transaction is 0.5% but not less than 0.05€.
 */
public class DefaultPricingRule implements CommissionRule {
    private final BigDecimal transactionPrice;
    private final BigDecimal lowestFee;

    public DefaultPricingRule(BigDecimal transactionPrice, BigDecimal lowestFee) {
        this.transactionPrice = transactionPrice;
        this.lowestFee = lowestFee;
    }

    @Override
    public boolean shouldApply(TransactionRequest params) {
        return true;
    }

    @Override
    public BigDecimal calculateCommission(TransactionRequest params) {
        BigDecimal commission = params.getAmount().multiply(transactionPrice);
        return commission.compareTo(lowestFee) > 0 ? commission : lowestFee;
    }
}
