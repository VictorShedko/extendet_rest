package com.epam.esm.gift_extended.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.epam.esm.gift_extended.exception.ErrorMassageByException;
import com.epam.esm.gift_extended.exception.JSONExceptionEntity;

@ControllerAdvice
public class GlobalExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleGiftException(Exception ce, WebRequest request) {
        System.err.println(ce);
        JSONExceptionEntity entity = ErrorMassageByException.createJSONExceptionEntity(ce);
        return new ResponseEntity<>(entity, new HttpHeaders(), entity.getStatus());
    }

    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        JSONExceptionEntity entity = new JSONExceptionEntity(404, "miss", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(entity, headers, entity.getStatus());
    }

}
