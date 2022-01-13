package io.xmljim.algorithms.functions.statistics.server.controller;

import io.xmljim.algorithms.services.entity.statistics.DataSet;
import io.xmljim.algorithms.services.entity.statistics.DataVector;
import io.xmljim.algorithms.functions.statistics.server.exception.InvalidDataException;
import io.xmljim.algorithms.functions.statistics.server.services.StatisticsService;
import io.xmljim.algorithms.services.entity.DefaultServiceResponse;
import io.xmljim.algorithms.services.entity.ServiceResponse;
import io.xmljim.algorithms.services.entity.statistics.ModelResult;
import io.xmljim.algorithms.services.entity.statistics.ScalarFunctionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
@SuppressWarnings("unused")
public class StatisticsController {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    StatisticsService statisticsService;

    @PostMapping("/sum/raw")
    public ServiceResponse<ScalarFunctionResult> calculateSumRaw(@RequestBody List<Number> data) {
        ScalarFunctionResult result = statisticsService.getSum(new DataVector("x", "dataX", data));
        return new DefaultServiceResponse<>(result);
    }

    @PostMapping("/sum")
    public ServiceResponse<ScalarFunctionResult> calculateSum(@RequestBody DataVector data) {
        ScalarFunctionResult result = statisticsService.getSum(data);
        return new DefaultServiceResponse<>(result);
    }

    @PostMapping("/mean/raw")
    public ServiceResponse<ScalarFunctionResult> calculateMeanRaw(@RequestBody List<Number> data) {
        ScalarFunctionResult result = statisticsService.getMean(new DataVector("x", "dataX", data));
        return new DefaultServiceResponse<>(result);
    }

    @PostMapping("/mean")
    public ServiceResponse<ScalarFunctionResult> calculateMean(@RequestBody DataVector data) {
        ScalarFunctionResult result = statisticsService.getMean(data);
        return new DefaultServiceResponse<>(result);
    }

    @PostMapping("/median/raw")
    public ServiceResponse<ScalarFunctionResult> calculateMedianRaw(@RequestBody List<Number> data) {
        ScalarFunctionResult result = statisticsService.getMedian(new DataVector("x", "dataX", data));
        return new DefaultServiceResponse<>(result);
    }

    @PostMapping("/median")
    public ServiceResponse<ScalarFunctionResult> calculateMedian(@RequestBody DataVector data) {
        ScalarFunctionResult result = statisticsService.getMedian(data);
        return new DefaultServiceResponse<>(result);
    }

    @PostMapping("/standard-deviation/raw")
    public ServiceResponse<ScalarFunctionResult> calculateStandardDeviationRaw(@RequestBody List<Number> data) {
        ScalarFunctionResult result = statisticsService.getStandardDeviation(new DataVector("x", "dataX", data));
        return new DefaultServiceResponse<>(result);
    }

    @PostMapping("/standard-deviation")
    public ServiceResponse<ScalarFunctionResult> calculateStandardDeviation(@RequestBody DataVector data) {
        ScalarFunctionResult result = statisticsService.getStandardDeviation(data);
        return new DefaultServiceResponse<>(result);
    }

    @PostMapping("/variance/raw")
    public ServiceResponse<ScalarFunctionResult> calculateVarianceRaw(@RequestBody List<Number> data) {
        ScalarFunctionResult result = statisticsService.getVariance(new DataVector("x", "dataX", data));
        return new DefaultServiceResponse<>(result);
    }

    @PostMapping("/variance")
    public ServiceResponse<ScalarFunctionResult> calculateVariance(@RequestBody DataVector data) {
        ScalarFunctionResult result = statisticsService.getVariance(data);
        return new DefaultServiceResponse<>(result);
    }

    @PostMapping("/covariance/raw")
    public ServiceResponse<ScalarFunctionResult> calculateCovarianceRaw(@RequestBody Map<String, List<Number>> data) {
        DataSet dataSet = new DataSet();

        if (data.containsKey("x")) {
            dataSet.addDataVector(new DataVector("x", "dataX", data.get("x")));
        } else {
            throw new InvalidDataException("Expected to have list of data for the 'x' variable. Not found.");
        }

        if (data.containsKey("y")) {
            dataSet.addDataVector(new DataVector("y", "dataY", data.get("y")));
        } else {
            throw new InvalidDataException("Expected to have a data list for the 'y' variable. Not found.");
        }

        ScalarFunctionResult result = statisticsService.getCovariance(dataSet);
        return new DefaultServiceResponse<>(result);
    }

    @PostMapping("/covariance")
    public ServiceResponse<ScalarFunctionResult> calculateCovariance(@RequestBody DataSet dataSet) {
        logger.debug("DataSet: {}", dataSet);

        if (!dataSet.hasVariable("x")) {
            throw new InvalidDataException("Expected data for variable 'x'. Not found");
        }

        if (!dataSet.hasVariable("y")) {
            throw new InvalidDataException("Expected data for variable 'y'. Not found");
        }

        ScalarFunctionResult result = statisticsService.getCovariance(dataSet);
        return new DefaultServiceResponse<>(result);
    }

    @PostMapping("/linear-regression/raw")
    public ServiceResponse<ModelResult> calculateLinearRegressionRaw(@RequestBody Map<String, List<Number>> data) {
        DataSet dataSet = new DataSet();

        if (data.containsKey("x")) {
            dataSet.addDataVector(new DataVector("x", "dataX", data.get("x")));
        } else {
            throw new InvalidDataException("Expected to have list of data for the 'x' variable. Not found.");
        }

        if (data.containsKey("y")) {
            dataSet.addDataVector(new DataVector("y", "dataY", data.get("y")));
        } else {
            throw new InvalidDataException("Expected to have a data list for the 'y' variable. Not found.");
        }

        ModelResult result = statisticsService.getLinearRegressionModel(dataSet);
        return new DefaultServiceResponse<>(result);
    }

    @PostMapping("/linear-regression")
    public ServiceResponse<ModelResult> calculateLinearRegression(@RequestBody DataSet dataSet) {
        logger.info("Received Request: {}", dataSet);
        if (!dataSet.hasVariable("x")) {
            logger.error("Expected data for variable 'x'. Not found: {}", dataSet);
            throw new InvalidDataException("Expected data for variable 'x'. Not found");
        }

        if (!dataSet.hasVariable("y")) {
            logger.error("Expected data for variable 'y'. Not found: {}", dataSet);
            throw new InvalidDataException("Expected data for variable 'y'. Not found");
        }

        ModelResult result = statisticsService.getLinearRegressionModel(dataSet);
        logger.info("Result: {}", result);
        return new DefaultServiceResponse<>(result);
    }
}
