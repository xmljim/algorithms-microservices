package io.xmljim.algorithms.service.retirement;

import io.xmljim.algorithms.functions.common.provider.FunctionProvider;
import io.xmljim.algorithms.functions.financial.provider.FinancialProvider;
import io.xmljim.algorithms.model.provider.ModelProvider;
import io.xmljim.algorithms.model.util.Version;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicReference;

@Configuration
@Slf4j
public class Config {
    @Value("${algorithms.financial.provider.version}")
    String financialProviderVersion;

    @Value("${algorithms.financial.provider.name}")
    String financialProviderName;

    @Bean
    FinancialProvider financialProvider() {
        log.info("Required: [name: {}, version: {}]", financialProviderName, financialProviderVersion);
        Iterable<FunctionProvider> functionProviderIterator = ServiceLoader.load(FunctionProvider.class);
        Version compareVersion = new Version(financialProviderVersion);
        final AtomicReference<FinancialProvider> provider = new AtomicReference<>();

        functionProviderIterator.forEach(fp -> {
            boolean isCompatible = compareVersion.isCompatibleWith(fp.getProviderVersion());

            if (fp.getProviderName().equals(financialProviderName) &&
                    compareVersion.isCompatibleWith(fp.getProviderVersion())) {
                log.info("Set FunctionProvider [name: {}, version: {}]", fp.getProviderName(), fp.getProviderVersion());
                provider.set((FinancialProvider) fp);
            }
        });

        if (provider.get() == null) {
            log.error("No compatible function provider found");
        }

        return provider.get();
    }

    @Bean
    ModelProvider modelProvider() {
        Iterable<ModelProvider> modelProviders = ServiceLoader.load(ModelProvider.class);

        if (modelProviders.iterator().hasNext()) {
            return modelProviders.iterator().next();
        } else {
            log.error("No Model Provider Found");
            return null;
        }
    }

    @Bean
    @LoadBalanced
    public RestTemplate cpiRestTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean
    @LoadBalanced
    public RestTemplate statisticsRestTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean
    @LoadBalanced
    public RestTemplate stocksRestTemplate() {
        return new RestTemplateBuilder().build();
    }
}
