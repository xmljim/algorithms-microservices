package io.xmljim.algorithms.functions.statistics.server.exception;

import io.xmljim.algorithms.services.entity.exception.HttpStatusCode;
import io.xmljim.algorithms.services.entity.exception.RequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidDataException extends RequestException {
    public InvalidDataException(final String message) {
        super(message, new HttpStatusCode(440, "INVALID_DATA_SUBMISSION", "Received Invalid Data"));
    }

    public InvalidDataException(final String message, final Throwable cause) {
        super(message, cause, new HttpStatusCode(440, "INVALID_DATA_SUBMISSION", "Received Invalid Data"));
    }


}
