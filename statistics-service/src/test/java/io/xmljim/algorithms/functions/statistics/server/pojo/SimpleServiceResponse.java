package io.xmljim.algorithms.functions.statistics.server.pojo;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SimpleServiceResponse {

    List<SimpleServiceResult> results;

    int count;
}
