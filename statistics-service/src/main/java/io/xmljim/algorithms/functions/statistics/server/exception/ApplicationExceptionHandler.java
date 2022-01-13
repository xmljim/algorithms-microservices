package io.xmljim.algorithms.functions.statistics.server.exception;

import io.xmljim.algorithms.services.entity.exception.HttpStatusCode;
import io.xmljim.algorithms.services.entity.exception.RequestError;
import io.xmljim.algorithms.services.entity.exception.RequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Handler class for exceptions. Returns a response entity containing an {@link RequestError} body
 */
@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Most common exception to occur. Typically will be bad data in the form of missing
     * or malformed data structures.
     * @param exception the InvalidDataException
     * @return a response entity containing the data violation error.
     */
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<RequestError> handleInvalidDataException(InvalidDataException exception) {
        RequestError error = new RequestError(exception);
        return new ResponseEntity<>(error, null, error.getStatusCode());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<RequestError> handleNullPointerException(NullPointerException exception) {
        if(exception.getMessage().contains("\"this.vectors\" is null")) {
            return handleInvalidDataException(new InvalidDataException("Expected a DataVector class, but got a raw data array (or map of arrays). Did you use the wrong endpoint?", exception));
        } else {
            return handleInternalException(exception);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RequestError> handleInternalException(Exception e) {
        RequestException exception = new RequestException(e.getMessage(), e, new HttpStatusCode(500, "INTERNAL_SERVER_ERROR", "Whoops! That shouldn't have happened: Uncaught Server Error"));
        RequestError error = new RequestError(exception);
        return new ResponseEntity<>(error, null, error.getStatusCode());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        String message = "Undefined Endpoint or Invalid Method: " + ex.getHttpMethod() + " " + ex.getRequestURL();
        String reason = "Undefined Resource";


        RequestException exception = new RequestException(message, new HttpStatusCode(404, "NOT_FOUND",  reason));
        RequestError error = new RequestError(exception);


        return new ResponseEntity<>(error, headers, error.getStatusCode());
    }
}
