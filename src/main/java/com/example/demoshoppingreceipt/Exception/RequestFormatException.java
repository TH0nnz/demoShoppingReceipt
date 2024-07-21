package com.example.demoshoppingreceipt.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Description
 * @Name LocationDataNotFoundException
 * @Author xd_to
 * @Project demoShoppingReceipt
 * @Package com.example.demoshoppingreceipt.Exception
 * @Date 2024/7/21
 * @Version 1.0
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestFormatException extends RuntimeException {
    private String message;
    public RequestFormatException(String message) {
        this.message=message;
    }
    @Override
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }


}
