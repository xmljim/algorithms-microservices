package io.xmljim.algorithms.services.entity.exception;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HttpStatusCode {
    private int statusCode;
    private String statusName;
    private String reason;
}
