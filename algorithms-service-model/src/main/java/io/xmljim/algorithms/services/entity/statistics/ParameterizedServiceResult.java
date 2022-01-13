package io.xmljim.algorithms.services.entity.statistics;

import io.xmljim.algorithms.model.Parameterized;

import io.xmljim.algorithms.services.entity.AbstractServiceResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@ToString
public abstract class ParameterizedServiceResult extends AbstractServiceResult<Parameterized> {
    private String name;
    private List<ParameterInfo> parameters;
    
    
    public ParameterizedServiceResult(String description, Parameterized parameterized) {
        super(description);
        this.name = parameterized.getName();
        this.parameters = parameterized.stream()
                .map(parameter -> new ParameterInfo(parameter))
                .collect(Collectors.toList());
       
    }
}
