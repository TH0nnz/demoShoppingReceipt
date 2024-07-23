package com.example.demoshoppingreceipt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class DemoShoppingReceiptApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoShoppingReceiptApplication.class, args);
    }

}
