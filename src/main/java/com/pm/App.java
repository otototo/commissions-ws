package com.pm;

import com.pm.client.ExchangeRateClient;
import com.pm.core.CommissionService;
import com.pm.core.ExchangeService;
import com.pm.core.rules.ClientWithDiscountRule;
import com.pm.core.rules.CommissionRule;
import com.pm.core.rules.DefaultPricingRule;
import com.pm.core.rules.HighTurnoverDiscountRule;
import com.pm.db.TransactionStore;
import com.pm.resources.TransactionResource;
import io.dropwizard.Application;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.setup.Environment;
import org.apache.http.impl.client.CloseableHttpClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


public class App extends Application<AppConfiguration> {
    public static void main(String[] args) throws Exception {
        new App().run(args);
    }

    @Override
    public void run(AppConfiguration appConfiguration, Environment environment) {
        final CloseableHttpClient httpClient = new HttpClientBuilder(environment).using(appConfiguration.getHttpClientConfiguration())
                .build(getName());

        ExchangeRateClient client = new ExchangeRateClient(httpClient);
        ExchangeService exchangeService = new ExchangeService(client);

        TransactionStore transactionStore = new TransactionStore();
        List<CommissionRule> commissionRules = List.of(
                new DefaultPricingRule(BigDecimal.valueOf(0.005), BigDecimal.valueOf(0.05)),
                new ClientWithDiscountRule(BigDecimal.valueOf(0.05), Set.of(42)),
                new HighTurnoverDiscountRule(transactionStore, BigDecimal.valueOf(1000.00), BigDecimal.valueOf(0.03))
        );
        CommissionService commissionService = new CommissionService(transactionStore, commissionRules);

        TransactionResource transactionResource = new TransactionResource(exchangeService, commissionService);
        environment.jersey().register(transactionResource);
    }
}
