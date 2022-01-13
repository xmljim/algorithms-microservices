package io.xmljim.algorithms.service.retirement.client;

import io.xmljim.algorithms.services.entity.cpi.CPIComputedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class CPIClient {
    @Value("${services.dependencies.cpi.name}")
    public String CPI_SERVICE_NAME;

    @Value("${services.dependencies.cpi.path}")
    public String CPI_SERVICE_PATH;

    @Value("${services.dependencies.cpi.endpoints.inflationMultiplier.path}")
    public String INFLATION_PATH;


    @Value("${services.dependencies.cpi.endpoints.averageInflation.path}")
    public String AVG_INFLATION_PATH;

    @Autowired
    RestTemplate cpiRestTemplate;

    @Cacheable("inflation-multiplier")
    public CPIComputedResponse getInflationMultiplier(int year, int baseYear) {
        String url = "http://" + CPI_SERVICE_NAME + CPI_SERVICE_PATH + INFLATION_PATH;
        log.info("URL: {} [{}, {}]", url, year, baseYear);
        return cpiRestTemplate.getForObject(url, CPIComputedResponse.class, year, baseYear);
    }

    @Cacheable("inflation-rate")
    public CPIComputedResponse getAverageInflationRate(int lastNumberOfYears) {
        String url = "http://" + CPI_SERVICE_NAME + CPI_SERVICE_PATH + AVG_INFLATION_PATH;
        log.info("URL: {} [{}]", url, lastNumberOfYears);
        return cpiRestTemplate.getForObject(url, CPIComputedResponse.class, lastNumberOfYears);
    }





}
