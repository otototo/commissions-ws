package com.pm.core;

import com.pm.api.TransactionRequest;
import com.pm.core.rules.CommissionRule;
import com.pm.db.TransactionStore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CommissionService {
    private final TransactionStore transactionStore;
    private final List<CommissionRule> rules;

    public CommissionService(TransactionStore transactionStore, List<CommissionRule> rules) {
        this.transactionStore = transactionStore;
        this.rules = rules;
    }

    public BigDecimal calculateCommission(TransactionRequest params) {
        BigDecimal commission = rules.stream()
                .filter(commissionRule -> commissionRule.shouldApply(params))
                .map(commissionRule -> commissionRule.calculateCommission(params))
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);
        transactionStore.updateTransactionAmount(params);
        return commission;
    }
}

