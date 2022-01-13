package io.xmljim.algorithms.services.entity.statistics;

import io.xmljim.algorithms.model.Parameter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ParameterInfo {

    private String name;
    private String variable;
    private String parameterType;
    private Object value;

    public ParameterInfo(Parameter<?> parameter) {
        this.name = parameter.getName();
        this.variable = parameter.getVariable();
        this.parameterType = parameter.getParameterType().toString();
        this.value = parameter.getValue();
    }
}
