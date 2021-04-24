package co.com.demo.bills.exceptions;

import co.com.demo.bills.domain.exception.ResponseException;
import co.com.demo.bills.exceptions.bussines.BillNotFoundException;
import co.com.demo.bills.exceptions.bussines.ProductNotFoundException;
import co.com.demo.bills.exceptions.validation.ServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class HandlerException {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ResponseException> productError(ProductNotFoundException ex) {
        ResponseException response = new ResponseException();
        response.setCode("NOT_FOUND");
        response.setMessage(ex.getMessage());
        response.setTime(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BillNotFoundException.class)
    public ResponseEntity<ResponseException> billError(BillNotFoundException ex) {
        ResponseException response = new ResponseException();
        response.setCode("NOT_FOUND");
        response.setMessage(ex.getMessage());
        response.setTime(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ResponseException> serverError(ServerException ex) {
        ResponseException response = new ResponseException();
        response.setCode("INTERNAL_SERVER_ERROR");
        response.setMessage(ex.getMessage());
        response.setTime(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
