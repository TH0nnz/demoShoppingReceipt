package com.example.demoshoppingreceipt.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Description
 * @Name TestExceptionHandler
 * @Author xd_to
 * @Project demoShoppingReceipt
 * @Package com.example.demoshoppingreceipt.Exception
 * @Date 2024/7/21
 * @Version 1.0
 */
@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(LocationDataNotFoundException.class)
    public Object LocationDataNotFoundExceptionHandler(LocationDataNotFoundException e) {
        String o_message = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(o_message);
    }

    @ExceptionHandler(RequestFormatException.class)
    public Object RequestFormatExceptionHandler(RequestFormatException e) {
        String o_message = e.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(o_message);
    }

    @ExceptionHandler(CustomizationException.class)
    public Object CustomizationExceptionHandler(CustomizationException e) {
        String o_message = e.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(o_message);
    }

}
