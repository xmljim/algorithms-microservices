package io.xmljim.algorithms.services.entity.statistics;

import io.xmljim.algorithms.model.Model;
import io.xmljim.algorithms.model.Parameterized;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@ToString
public class ModelResult extends ParameterizedServiceResult {
    
    private List<CoefficientInfo> coefficients;
    
    public ModelResult(final Model model) {
        super(model.getName(), model);
        model.solve();
        coefficients = model.coefficients().map(CoefficientInfo::new).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public <T> T getValueByName(String name) {
        Optional<CoefficientInfo> coefficient = coefficients.stream().filter(coeff -> coeff.getName().equals(name)).findFirst();
        return coefficient.map(coefficientInfo -> (T) coefficientInfo.getValue()).orElse(null);
    }
}
