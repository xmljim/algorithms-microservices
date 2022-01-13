package io.xmljim.algorithms.services.entity.exception;

public class RequestException extends RuntimeException {
    private HttpStatusCode statusCode;

    public RequestException(final String message, final HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public RequestException(final String message, final Throwable cause, final HttpStatusCode statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

}
