package io.xmljim.algorithms.services.entity.statistics;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DataSet {

    private List<DataVector> vectors;


    public DataVector getDataVector(String variableName) {
        return vectors.stream()
                .filter(dataVector -> dataVector.getVariable().equals(variableName))
                .findFirst()
                .orElse(null);
    }

    public void addDataVector(DataVector vector) {
        if (vectors == null) {
            vectors = new ArrayList<>();
        }
        vectors.add(vector);
    }

    public boolean hasVariable(String variableName) {
        return getDataVector(variableName) != null;
    }
}
