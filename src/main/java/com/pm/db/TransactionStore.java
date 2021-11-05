package com.pm.db;

import com.pm.api.TransactionRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TransactionStore {
    private final Map<Integer, Map<LocalDate, BigDecimal>> clientToTransactionAmount = new HashMap<>();

    public BigDecimal getMonthlyTurnover(int clientId, LocalDate date) {
        LocalDate yearMonth = date.withDayOfMonth(1);
        return clientToTransactionAmount.computeIfAbsent(clientId, n -> new HashMap<>())
                .computeIfAbsent(yearMonth, n -> BigDecimal.ZERO);
    }

    public void updateTransactionAmount(TransactionRequest params) {
        LocalDate yearMonth = params.getDate().withDayOfMonth(1);
        Map<LocalDate, BigDecimal> monthToAmount = clientToTransactionAmount
                .computeIfAbsent(params.getClientId(), n -> new HashMap<>());
        BigDecimal old = monthToAmount.getOrDefault(yearMonth, BigDecimal.ZERO);
        BigDecimal totalSum = old.add(params.getAmount());
        monthToAmount.put(yearMonth, totalSum);
    }
}
