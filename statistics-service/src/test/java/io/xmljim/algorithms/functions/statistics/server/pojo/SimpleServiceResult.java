package io.xmljim.algorithms.functions.statistics.server.pojo;

import io.xmljim.algorithms.services.entity.statistics.CoefficientInfo;
import io.xmljim.algorithms.services.entity.statistics.ParameterInfo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SimpleServiceResult {
    private String name;
    private String type;
    private Object value;
    private List<ParameterInfo> parameters;
    private List<CoefficientInfo> coefficients;
}
