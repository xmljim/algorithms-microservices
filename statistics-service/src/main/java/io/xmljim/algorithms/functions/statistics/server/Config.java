package io.xmljim.algorithms.functions.statistics.server;

import io.xmljim.algorithms.functions.common.provider.FunctionProvider;
import io.xmljim.algorithms.functions.statistics.provider.StatisticsProvider;
import io.xmljim.algorithms.model.provider.ModelProvider;
import io.xmljim.algorithms.model.util.Version;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Configuration
public class Config {

    @Value("${algorithms.statistics.provider.version}")
    String statisticsProviderVersion;

    @Value("${algorithms.statistics.provider.name}")
    String statisticsProviderName;

    @Bean
    StatisticsProvider statisticsProvider() {
        log.info("Required: [name: {}, version: {}]", statisticsProviderName, statisticsProviderVersion);
        Iterable<FunctionProvider> functionProviderIterator = ServiceLoader.load(FunctionProvider.class);
        Version compareVersion = new Version(statisticsProviderVersion);
        final AtomicReference<StatisticsProvider> provider = new AtomicReference<>();

        functionProviderIterator.forEach(fp -> {
            boolean isCompatible = compareVersion.isCompatibleWith(fp.getProviderVersion());

            if (fp.getProviderName().equals(statisticsProviderName) &&
                    compareVersion.isCompatibleWith(fp.getProviderVersion())) {
                log.info("Set FunctionProvider [name: {}, version: {}]", fp.getProviderName(), fp.getProviderVersion());
                provider.set((StatisticsProvider) fp);
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
}
