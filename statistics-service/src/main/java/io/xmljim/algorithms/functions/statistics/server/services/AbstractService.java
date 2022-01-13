package io.xmljim.algorithms.functions.statistics.server.services;

import io.xmljim.algorithms.functions.statistics.provider.StatisticsProvider;
import io.xmljim.algorithms.model.provider.ModelProvider;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractService {
    @Autowired
    private StatisticsProvider statisticsProvider;

    @Autowired
    private ModelProvider modelProvider;

    public StatisticsProvider getStatisticsProvider() {
        return statisticsProvider;
    }

    public ModelProvider getModelProvider() {
        return modelProvider;
    }
}
