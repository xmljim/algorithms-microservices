package io.xmljim.algorithms.services.entity.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class RequestError {
    String statusName;
    int statusCode;
    private String message;
    private String reason;
    StackTraceElement[] stacktrace;
    String exceptionClass;

    public RequestError(RequestException exception) {
        this.statusName = exception.getStatusCode().getStatusName();
        this.statusCode = exception.getStatusCode().getStatusCode();
        this.reason = exception.getStatusCode().getReason();
        this.message = exception.getMessage();
        this.exceptionClass = exception.getClass().getSimpleName();
        this.stacktrace = exception.getStackTrace();
    }
}
