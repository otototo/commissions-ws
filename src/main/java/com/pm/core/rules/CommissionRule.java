package com.pm.core.rules;

import com.pm.api.TransactionRequest;

import java.math.BigDecimal;

public interface CommissionRule {
    boolean shouldApply(TransactionRequest params);

    BigDecimal calculateCommission(TransactionRequest params);
}
