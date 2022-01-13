package io.xmljim.algorithms.service.retirement.client;

import io.xmljim.algorithms.services.entity.statistics.DataSet;
import io.xmljim.algorithms.services.entity.statistics.DataVector;
import io.xmljim.algorithms.services.entity.statistics.ModelResponse;
import io.xmljim.algorithms.services.entity.statistics.ModelResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class StatisticsClient {

    @Autowired
    private RestTemplate statisticsRestTemplate;

    @Value("${services.dependencies.statistics.name}")
    private String STATISTICS_SERVICE_NAME;

    @Value("${services.dependencies.statistics.path}")
    private String STATISTICS_SERVICE_PATH;

    @Value("${services.dependencies.statistics.endpoints.linear-regression.path}")
    private String LINEAR_REGRESSION_PATH;

    public ModelResult getLinearRegressionModel(List<Number> dataX, List<Number> dataY) {
        DataSet dataSet = new DataSet();
        dataSet.addDataVector(new DataVector("x", "dataX", dataX));
        dataSet.addDataVector(new DataVector("y", "dataY", dataY));
        return getLinearRegressionModel(dataSet);
    }

    @Cacheable("regression")
    public ModelResult getLinearRegressionModel(DataSet dataSet) {
        String url = "http://" + STATISTICS_SERVICE_NAME + STATISTICS_SERVICE_PATH + LINEAR_REGRESSION_PATH;
        log.info("URL: {}", url);
        ModelResponse result = statisticsRestTemplate.postForObject(url, dataSet, ModelResponse.class);
        log.info("return {}", result);
        return result.getResults().get(0);
    }
}
