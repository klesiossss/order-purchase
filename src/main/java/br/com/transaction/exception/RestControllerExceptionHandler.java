package br.com.transaction.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<StandardError>> handleConstrainViolationException(ConstraintViolationException exception){

        List<StandardError> standardErrors = exception.getConstraintViolations().stream().map(constraintViolation -> {
            StandardError.StandardErrorBuilder builder = StandardError.builder();
             String[] decode = Objects.requireNonNull(constraintViolation.getMessage().split("#"));
            String code, message;
                code = decode[0];
                message = decode[1];
            decode = null;
            builder.code(Integer.valueOf(code)).detail(message).title("Error");
            return builder.build();

        }).collect(Collectors.toList());
        return new ResponseEntity<>(standardErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ ResourceNotFoundException.class })
    public ResponseEntity<Object> handleEntityNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ExceptionMessage exception = new ExceptionMessage(ex.getMessage(), HttpStatus.NOT_FOUND, null);
        return handleExceptionInternal(ex, exception, new HttpHeaders(), exception.getStatusCode(), request);
    }

    @ExceptionHandler({ TransactionException.class })
    public ResponseEntity<Object> handleTransactionException(TransactionException ex, WebRequest request) {
        ExceptionMessage exception = new ExceptionMessage(ex.getMessage(), HttpStatus.BAD_REQUEST, null);
        return handleExceptionInternal(ex, exception, new HttpHeaders(), exception.getStatusCode(), request);
    }


    private ExceptionMessage getExceptionMessageObject(MethodArgumentNotValidException ex) {
        List errors = new ArrayList<String>();
        ex.getBindingResult().getFieldErrors().forEach(field -> errors.add(field.getField() + ": " + field.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors().forEach(field -> errors.add(field.getObjectName() + ": " + field.getDefaultMessage()));
        return new ExceptionMessage("Error in validation", HttpStatus.BAD_REQUEST, errors);
    }

}
