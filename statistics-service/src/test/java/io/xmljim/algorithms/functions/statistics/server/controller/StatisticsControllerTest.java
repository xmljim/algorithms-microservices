package io.xmljim.algorithms.functions.statistics.server.controller;

import io.xmljim.algorithms.services.entity.statistics.DataSet;
import io.xmljim.algorithms.services.entity.statistics.DataVector;
import io.xmljim.algorithms.functions.statistics.server.pojo.SimpleServiceResponse;
import io.xmljim.algorithms.functions.statistics.server.pojo.SimpleServiceResult;
import io.xmljim.algorithms.services.entity.statistics.CoefficientInfo;
import io.xmljim.algorithms.services.entity.statistics.ParameterInfo;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                properties = "spring.cloud.config.enabled=false")
@ActiveProfiles("test")
class StatisticsControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsControllerTest.class);

    @Autowired
    StatisticsController statisticsController;

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;


    private String buildPath(String function, boolean isRaw) {
        StringBuilder builder = new StringBuilder();
        builder.append("http://localhost:")
                .append(port)
                .append("/statistics/")
                .append(function);
        if (isRaw) {
            builder.append("/raw");
        }

        logger.info("URL: {}", builder.toString());
        return builder.toString();
    }

    @Test
    @DisplayName("context loads...")
    void testContextLoads() {
        assertThat(statisticsController).isNotNull();
    }

    @Test
    @DisplayName("Test Raw Sum")
    void calculateSumRaw() {
        List<Number> rawData = List.of(1,2,3,4,5);
        RequestEntity<List<Number>> entity = RequestEntity.post(buildPath("sum", true))
                .accept(MediaType.APPLICATION_JSON)
                .body(rawData);

        ResponseEntity<SimpleServiceResponse> response = restTemplate.exchange(entity, SimpleServiceResponse.class);
        SimpleServiceResponse theResponse = response.getBody();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(theResponse.getCount()).isEqualTo(1);

        SimpleServiceResult result = theResponse.getResults().get(0);
        assertThat(result.getName()).isEqualTo("sum");
        assertThat(result.getParameters().size()).isEqualTo(1);
        assertThat((double)result.getValue()).isEqualTo(15.0d);

        ParameterInfo parameter = result.getParameters().get(0);
        assertThat(parameter.getName()).isEqualTo("dataX");
        assertThat(parameter.getVariable()).isEqualTo("x");
        assertThat(parameter.getParameterType()).isEqualTo("SCALAR_VECTOR");
    }

    @Test
    @DisplayName("Test DataVector Sum")
    void calculateSum() {

        List<Number> rawData = List.of(1,2,3,4,5);
        DataVector vector = new DataVector("x", "mydata", rawData);
        RequestEntity<DataVector> entity = RequestEntity.post(buildPath("sum", false))
                .accept(MediaType.APPLICATION_JSON)
                .body(vector);

        ResponseEntity<SimpleServiceResponse> response = restTemplate.exchange(entity, SimpleServiceResponse.class);
        SimpleServiceResponse theResponse = response.getBody();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(theResponse.getCount()).isEqualTo(1);

        SimpleServiceResult result = theResponse.getResults().get(0);
        assertThat(result.getName()).isEqualTo("sum");
        assertThat(result.getParameters().size()).isEqualTo(1);
        assertThat((double)result.getValue()).isEqualTo(15.0d);

        ParameterInfo parameter = result.getParameters().get(0);
        assertThat(parameter.getName()).isEqualTo("mydata");
        assertThat(parameter.getVariable()).isEqualTo("x");
        assertThat(parameter.getParameterType()).isEqualTo("SCALAR_VECTOR");
    }

    @Test
    @DisplayName("Test Raw Mean")
    void calculateMeanRaw() {
        List<Number> rawData = List.of(1,2,3,4,5);
        RequestEntity<List<Number>> entity = RequestEntity.post(buildPath("mean", true))
                .accept(MediaType.APPLICATION_JSON)
                .body(rawData);

        ResponseEntity<SimpleServiceResponse> response = restTemplate.exchange(entity, SimpleServiceResponse.class);
        SimpleServiceResponse theResponse = response.getBody();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(theResponse.getCount()).isEqualTo(1);

        SimpleServiceResult result = theResponse.getResults().get(0);
        assertThat(result.getName()).isEqualTo("mean");
        assertThat(result.getParameters().size()).isEqualTo(1);
        assertThat((double)result.getValue()).isEqualTo(3.0d);

        ParameterInfo parameter = result.getParameters().get(0);
        assertThat(parameter.getName()).isEqualTo("dataX");
        assertThat(parameter.getVariable()).isEqualTo("x");
        assertThat(parameter.getParameterType()).isEqualTo("SCALAR_VECTOR");
    }

    @Test
    @DisplayName("Test DataVector Mean")
    void calculateMean() {
        List<Number> rawData = List.of(1,2,3,4,5);
        DataVector vector = new DataVector("x", "mydata", rawData);
        RequestEntity<DataVector> entity = RequestEntity.post(buildPath("mean", false))
                .accept(MediaType.APPLICATION_JSON)
                .body(vector);

        ResponseEntity<SimpleServiceResponse> response = restTemplate.exchange(entity, SimpleServiceResponse.class);
        SimpleServiceResponse theResponse = response.getBody();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(theResponse.getCount()).isEqualTo(1);

        SimpleServiceResult result = theResponse.getResults().get(0);
        assertThat(result.getName()).isEqualTo("mean");
        assertThat(result.getParameters().size()).isEqualTo(1);
        assertThat((double)result.getValue()).isEqualTo(3.0d);

        ParameterInfo parameter = result.getParameters().get(0);
        assertThat(parameter.getName()).isEqualTo("mydata");
        assertThat(parameter.getVariable()).isEqualTo("x");
        assertThat(parameter.getParameterType()).isEqualTo("SCALAR_VECTOR");
    }

    @Test
    @DisplayName("Test Raw Median")
    void calculateMedianRaw() {
        List<Number> rawData = List.of(1,2,3,4,5);
        RequestEntity<List<Number>> entity = RequestEntity.post(buildPath("median", true))
                .accept(MediaType.APPLICATION_JSON)
                .body(rawData);

        ResponseEntity<SimpleServiceResponse> response = restTemplate.exchange(entity, SimpleServiceResponse.class);
        SimpleServiceResponse theResponse = response.getBody();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(theResponse.getCount()).isEqualTo(1);

        SimpleServiceResult result = theResponse.getResults().get(0);
        assertThat(result.getName()).isEqualTo("median");
        assertThat(result.getParameters().size()).isEqualTo(1);
        assertThat((double)result.getValue()).isEqualTo(3.0d);

        ParameterInfo parameter = result.getParameters().get(0);
        assertThat(parameter.getName()).isEqualTo("dataX");
        assertThat(parameter.getVariable()).isEqualTo("x");
        assertThat(parameter.getParameterType()).isEqualTo("SCALAR_VECTOR");
    }

    @Test
    @DisplayName("Test DataVector Median")
    void calculateMedian() {
        List<Number> rawData = List.of(1,2,3,4,5);
        DataVector vector = new DataVector("x", "mydata", rawData);
        RequestEntity<DataVector> entity = RequestEntity.post(buildPath("median", false))
                .accept(MediaType.APPLICATION_JSON)
                .body(vector);

        ResponseEntity<SimpleServiceResponse> response = restTemplate.exchange(entity, SimpleServiceResponse.class);
        SimpleServiceResponse theResponse = response.getBody();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(theResponse.getCount()).isEqualTo(1);

        SimpleServiceResult result = theResponse.getResults().get(0);
        assertThat(result.getName()).isEqualTo("median");
        assertThat(result.getParameters().size()).isEqualTo(1);
        assertThat((double)result.getValue()).isEqualTo(3.0d);

        ParameterInfo parameter = result.getParameters().get(0);
        assertThat(parameter.getName()).isEqualTo("mydata");
        assertThat(parameter.getVariable()).isEqualTo("x");
        assertThat(parameter.getParameterType()).isEqualTo("SCALAR_VECTOR");
    }

    @Test
    @DisplayName("Test Raw Standard Deviation")
    void calculateStandardDeviationRaw() {
        List<Number> rawData = List.of(1,2,3,4,5);
        RequestEntity<List<Number>> entity = RequestEntity.post(buildPath("standard-deviation", true))
                .accept(MediaType.APPLICATION_JSON)
                .body(rawData);

        ResponseEntity<SimpleServiceResponse> response = restTemplate.exchange(entity, SimpleServiceResponse.class);
        SimpleServiceResponse theResponse = response.getBody();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(theResponse.getCount()).isEqualTo(1);

        SimpleServiceResult result = theResponse.getResults().get(0);
        assertThat(result.getName()).isEqualTo("standardDeviation");
        assertThat(result.getParameters().size()).isEqualTo(1);
        assertThat((double)result.getValue()).isEqualTo(1.581d, Offset.offset(.001d));

        ParameterInfo parameter = result.getParameters().get(0);
        assertThat(parameter.getName()).isEqualTo("dataX");
        assertThat(parameter.getVariable()).isEqualTo("x");
        assertThat(parameter.getParameterType()).isEqualTo("SCALAR_VECTOR");
    }

    @Test
    @DisplayName("Test DataVector Standard Deviation")
    void calculateStandardDeviation() {
        List<Number> rawData = List.of(1,2,3,4,5);
        DataVector dataVector = new DataVector("x", "mydata", rawData);
        RequestEntity<DataVector> entity = RequestEntity.post(buildPath("standard-deviation", false))
                .accept(MediaType.APPLICATION_JSON)
                .body(dataVector);

        ResponseEntity<SimpleServiceResponse> response = restTemplate.exchange(entity, SimpleServiceResponse.class);
        SimpleServiceResponse theResponse = response.getBody();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(theResponse.getCount()).isEqualTo(1);

        SimpleServiceResult result = theResponse.getResults().get(0);
        assertThat(result.getName()).isEqualTo("standardDeviation");
        assertThat(result.getParameters().size()).isEqualTo(1);
        assertThat((double)result.getValue()).isEqualTo(1.581d, Offset.offset(.001d));

        ParameterInfo parameter = result.getParameters().get(0);
        assertThat(parameter.getName()).isEqualTo("mydata");
        assertThat(parameter.getVariable()).isEqualTo("x");
        assertThat(parameter.getParameterType()).isEqualTo("SCALAR_VECTOR");
    }

    @Test
    @DisplayName("Test Raw Variance")
    void calculateVarianceRaw() {
        List<Number> rawData = List.of(1,2,3,4,5);
        RequestEntity<List<Number>> entity = RequestEntity.post(buildPath("variance", true))
                .accept(MediaType.APPLICATION_JSON)
                .body(rawData);

        ResponseEntity<SimpleServiceResponse> response = restTemplate.exchange(entity, SimpleServiceResponse.class);
        SimpleServiceResponse theResponse = response.getBody();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(theResponse.getCount()).isEqualTo(1);

        SimpleServiceResult result = theResponse.getResults().get(0);
        assertThat(result.getName()).isEqualTo("variance");
        assertThat(result.getParameters().size()).isEqualTo(1);
        assertThat((double)result.getValue()).isEqualTo(2.50d, Offset.offset(.001d));

        ParameterInfo parameter = result.getParameters().get(0);
        assertThat(parameter.getName()).isEqualTo("dataX");
        assertThat(parameter.getVariable()).isEqualTo("x");
        assertThat(parameter.getParameterType()).isEqualTo("SCALAR_VECTOR");

    }

    @Test
    @DisplayName("Test DataVector Variance")
    void calculateVariance() {
        List<Number> rawData = List.of(1,2,3,4,5);
        DataVector vector = new DataVector("x", "mydata", rawData);
        RequestEntity<DataVector> entity = RequestEntity.post(buildPath("variance", false))
                .accept(MediaType.APPLICATION_JSON)
                .body(vector);

        ResponseEntity<SimpleServiceResponse> response = restTemplate.exchange(entity, SimpleServiceResponse.class);
        SimpleServiceResponse theResponse = response.getBody();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(theResponse.getCount()).isEqualTo(1);

        SimpleServiceResult result = theResponse.getResults().get(0);
        assertThat(result.getName()).isEqualTo("variance");
        assertThat(result.getParameters().size()).isEqualTo(1);
        assertThat((double)result.getValue()).isEqualTo(2.50d, Offset.offset(.001d));

        ParameterInfo parameter = result.getParameters().get(0);
        assertThat(parameter.getName()).isEqualTo("mydata");
        assertThat(parameter.getVariable()).isEqualTo("x");
        assertThat(parameter.getParameterType()).isEqualTo("SCALAR_VECTOR");

    }

    @Test
    @DisplayName("Test Raw Covariance")
    void calculateCovarianceRaw() {
        Map<String, List<Number>> dataMap = new HashMap<>();
        dataMap.put("x", List.of(1, 2, 3, 4, 5));
        dataMap.put("y", List.of(6, 7, 8, 9, 10));

        RequestEntity<Map<String, List<Number>>> entity = RequestEntity.post(buildPath("covariance", true))
                .accept(MediaType.APPLICATION_JSON)
                .body(dataMap);

        ResponseEntity<SimpleServiceResponse> response = restTemplate.exchange(entity, SimpleServiceResponse.class);
        SimpleServiceResponse theResponse = response.getBody();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(theResponse.getCount()).isEqualTo(1);

        SimpleServiceResult result = theResponse.getResults().get(0);
        assertThat(result.getName()).isEqualTo("covariance");
        assertThat(result.getParameters().size()).isEqualTo(2);
        assertThat((double)result.getValue()).isEqualTo(2.50d, Offset.offset(.01d));

        ParameterInfo parameter = result.getParameters().get(0);
        assertThat(parameter.getName()).isEqualTo("dataX");
        assertThat(parameter.getVariable()).isEqualTo("x");
        assertThat(parameter.getParameterType()).isEqualTo("SCALAR_VECTOR");

        ParameterInfo parameter2 = result.getParameters().get(1);
        assertThat(parameter2.getName()).isEqualTo("dataY");
        assertThat(parameter2.getVariable()).isEqualTo("y");
        assertThat(parameter2.getParameterType()).isEqualTo("SCALAR_VECTOR");
    }

    @Test
    @DisplayName("Test DataVector Covariance")
    void calculateCovariance() {
        DataSet dataSet = new DataSet();
        dataSet.addDataVector(new DataVector("x", "data1", List.of(1, 2, 3, 4, 5)));
        dataSet.addDataVector(new DataVector("y", "data2", List.of(5, 6, 7, 8, 9, 10)));

        RequestEntity<DataSet> entity = RequestEntity.post(buildPath("covariance", false))
                .accept(MediaType.APPLICATION_JSON)
                .body(dataSet);

        ResponseEntity<SimpleServiceResponse> response = restTemplate.exchange(entity, SimpleServiceResponse.class);
        SimpleServiceResponse theResponse = response.getBody();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(theResponse.getCount()).isEqualTo(1);

        SimpleServiceResult result = theResponse.getResults().get(0);
        assertThat(result.getName()).isEqualTo("covariance");
        assertThat(result.getParameters().size()).isEqualTo(2);
        assertThat((double)result.getValue()).isEqualTo(2.50d, Offset.offset(.01d));

        ParameterInfo parameter = result.getParameters().get(0);
        assertThat(parameter.getName()).isEqualTo("data1");
        assertThat(parameter.getVariable()).isEqualTo("x");
        assertThat(parameter.getParameterType()).isEqualTo("SCALAR_VECTOR");

        ParameterInfo parameter2 = result.getParameters().get(1);
        assertThat(parameter2.getName()).isEqualTo("data2");
        assertThat(parameter2.getVariable()).isEqualTo("y");
        assertThat(parameter2.getParameterType()).isEqualTo("SCALAR_VECTOR");
    }

    @Test
    void calculateLinearRegressionRaw() {

        List<Number> year = List.of(1991, 1992, 1993, 1994, 1995, 1996, 1997, 1998, 1999, 2000, 2001, 2002, 2003, 2004, 2005, 2006,
                2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021);
        List<Number> logStockPrice = List.of(2.935995698,3.043157856,3.227385306,3.239644775,3.541066024,3.713553541,3.853685438,
                3.968205472,4.137556334,4.051904648,3.991702652,3.860198411,4.011143974,4.035873013,4.035902601,4.113523889,4.148484503,
                3.900223979,4.021313779,4.085602002,4.106225585,4.153526763,4.285384457,4.328557567,4.325661156,4.381570986,4.491178551,
                4.464791988,4.573249297,4.642875273,4.716025559);

        Map<String, List<Number>> dataMap = new HashMap<>();
        dataMap.put("x", year);
        dataMap.put("y", logStockPrice);

        RequestEntity<Map<String, List<Number>>> entity = RequestEntity.post(buildPath("linear-regression", true))
                .accept(MediaType.APPLICATION_JSON)
                .body(dataMap);

        ResponseEntity<SimpleServiceResponse> response = restTemplate.exchange(entity, SimpleServiceResponse.class);
        SimpleServiceResponse theResponse = response.getBody();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(theResponse.getCount()).isEqualTo(1);

        SimpleServiceResult result = theResponse.getResults().get(0);
        assertThat(result.getName()).isEqualTo("linearRegressionModel");
        assertThat(result.getParameters().size()).isEqualTo(2);

        ParameterInfo parameter = result.getParameters().get(0);
        assertThat(parameter.getName()).isEqualTo("dataX");
        assertThat(parameter.getVariable()).isEqualTo("x");
        assertThat(parameter.getParameterType()).isEqualTo("SCALAR_VECTOR");

        ParameterInfo parameter2 = result.getParameters().get(1);
        assertThat(parameter2.getName()).isEqualTo("dataY");
        assertThat(parameter2.getVariable()).isEqualTo("y");
        assertThat(parameter2.getParameterType()).isEqualTo("SCALAR_VECTOR");

        List<CoefficientInfo> coefficients = theResponse.getResults().get(0).getCoefficients();
        assertThat(coefficients.size()).isGreaterThanOrEqualTo(7);
        assertThat(coefficients.stream().anyMatch(c -> c.getName().equals("slope"))).isTrue();
        assertThat(coefficients.stream().anyMatch(c -> c.getName().equals("intercept"))).isTrue();
        assertThat(coefficients.stream().anyMatch(c -> c.getName().equals("rSquared"))).isTrue();
    }

    @Test
    void calculateLinearRegression() {
        DataSet dataSet = new DataSet();

        DataVector year = new DataVector("x", "year", List.of(1991, 1992, 1993, 1994, 1995, 1996, 1997, 1998, 1999, 2000, 2001, 2002, 2003, 2004, 2005, 2006,
                2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021));

        DataVector logStockPrice = new DataVector("y", "logStockPrice", List.of(2.935995698,3.043157856,3.227385306,3.239644775,3.541066024,3.713553541,3.853685438,
                3.968205472,4.137556334,4.051904648,3.991702652,3.860198411,4.011143974,4.035873013,4.035902601,4.113523889,4.148484503,
                3.900223979,4.021313779,4.085602002,4.106225585,4.153526763,4.285384457,4.328557567,4.325661156,4.381570986,4.491178551,
                4.464791988,4.573249297,4.642875273,4.716025559));

        dataSet.addDataVector(year);
        dataSet.addDataVector(logStockPrice);

        RequestEntity<DataSet> entity = RequestEntity.post(buildPath("linear-regression", false))
                .accept(MediaType.APPLICATION_JSON)
                .body(dataSet);

        ResponseEntity<SimpleServiceResponse> response = restTemplate.exchange(entity, SimpleServiceResponse.class);
        SimpleServiceResponse theResponse = response.getBody();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(theResponse.getCount()).isEqualTo(1);

        SimpleServiceResult result = theResponse.getResults().get(0);
        assertThat(result.getName()).isEqualTo("linearRegressionModel");
        assertThat(result.getParameters().size()).isEqualTo(2);

        ParameterInfo parameter = result.getParameters().get(0);
        assertThat(parameter.getName()).isEqualTo("year");
        assertThat(parameter.getVariable()).isEqualTo("x");
        assertThat(parameter.getParameterType()).isEqualTo("SCALAR_VECTOR");

        ParameterInfo parameter2 = result.getParameters().get(1);
        assertThat(parameter2.getName()).isEqualTo("logStockPrice");
        assertThat(parameter2.getVariable()).isEqualTo("y");
        assertThat(parameter2.getParameterType()).isEqualTo("SCALAR_VECTOR");

        List<CoefficientInfo> coefficients = theResponse.getResults().get(0).getCoefficients();
        assertThat(coefficients.size()).isGreaterThanOrEqualTo(7);
        assertThat(coefficients.stream().anyMatch(c -> c.getName().equals("slope"))).isTrue();
        assertThat(coefficients.stream().anyMatch(c -> c.getName().equals("intercept"))).isTrue();
        assertThat(coefficients.stream().anyMatch(c -> c.getName().equals("rSquared"))).isTrue();
    }
}