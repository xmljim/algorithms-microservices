package io.xmljim.algorithms.functions.statistics.server.services;

import io.xmljim.algorithms.services.entity.statistics.DataSet;
import io.xmljim.algorithms.services.entity.statistics.DataVector;
import io.xmljim.algorithms.model.Model;
import io.xmljim.algorithms.model.ScalarVector;
import io.xmljim.algorithms.services.entity.statistics.ModelResult;
import io.xmljim.algorithms.services.entity.statistics.ScalarFunctionResult;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService extends AbstractService {

    public ScalarFunctionResult getSum(DataVector data) {
        ScalarVector vector = getModelProvider().getVectorFactory().createScalarVector(data.getName(), data.getVariable() , data.getData());
        return new ScalarFunctionResult(getStatisticsProvider().getFactory().sum(vector));
    }

    public ScalarFunctionResult getMean(DataVector data) {
        ScalarVector vector = getModelProvider().getVectorFactory().createScalarVector(data.getName(), data.getVariable() , data.getData());
        return new ScalarFunctionResult(getStatisticsProvider().getFactory().mean(vector));
    }

    public ScalarFunctionResult getMedian(DataVector data) {
        ScalarVector vector = getModelProvider().getVectorFactory().createScalarVector(data.getName(), data.getVariable() , data.getData());
        return new ScalarFunctionResult(getStatisticsProvider().getFactory().median(vector));
    }

    public ScalarFunctionResult getVariance(DataVector data) {
        ScalarVector vector = getModelProvider().getVectorFactory().createScalarVector(data.getName(), data.getVariable() , data.getData());
        return new ScalarFunctionResult(getStatisticsProvider().getFactory().variance(vector));
    }

    public ScalarFunctionResult getStandardDeviation(DataVector data) {
        ScalarVector vector = getModelProvider().getVectorFactory().createScalarVector(data.getName(), data.getVariable() , data.getData());
        return new ScalarFunctionResult(getStatisticsProvider().getFactory().standardDeviation(vector));
    }

    public ScalarFunctionResult getCovariance(DataSet data) {
        DataVector dataX = data.getDataVector("x");
        DataVector dataY = data.getDataVector("y");
        ScalarVector vectorX = getModelProvider().getVectorFactory().createScalarVector(dataX.getName(), dataX.getVariable(), dataX.getData());
        ScalarVector vectorY = getModelProvider().getVectorFactory().createScalarVector(dataY.getName(), dataY.getVariable(), dataY.getData());
        return new ScalarFunctionResult(getStatisticsProvider().getFactory().covariance(vectorX, vectorY));
    }

    public ModelResult getLinearRegressionModel(DataSet data) {
        DataVector dataX = data.getDataVector("x");
        DataVector dataY = data.getDataVector("y");
        ScalarVector vectorX = getModelProvider().getVectorFactory().createScalarVector(dataX.getName(), dataX.getVariable(), dataX.getData());
        ScalarVector vectorY = getModelProvider().getVectorFactory().createScalarVector(dataY.getName(), dataY.getVariable(), dataY.getData());

        Model model = getStatisticsProvider().getFactory().linearRegression(vectorX, vectorY);
        model.solve();
        return new ModelResult(model);
    }


}
