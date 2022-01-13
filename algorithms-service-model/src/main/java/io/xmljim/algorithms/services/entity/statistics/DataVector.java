package io.xmljim.algorithms.services.entity.statistics;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DataVector {
    private String variable;
    private String name;
    List<Number> data;
}
