package com.example.demoshoppingreceipt.controller;


import com.example.demoshoppingreceipt.Exception.LocationDataNotFoundException;
import com.example.demoshoppingreceipt.Exception.RequestFormatException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @Description
 * @Name InvoiceControllerTest
 * @Author t0nn
 * @Project demoShoppingReceipt
 * @Package com.example.demoshoppingreceipt.controller
 * @Date 2024/7/21
 * @Version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
class InvoiceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private InvoiceController invoiceController;
    @Value("${ca.tax}")
    private Double ca_tex;

    @Value("${ca.exempt}")
    private ArrayList<String> ca_exempt;

    @Value("${ny.tax}")
    private Double ny_tex;

    @Value("${ny.exempt}")
    private ArrayList<String> ny_exempt;

    @Value("${list}")
    private ArrayList<String> list;

    @Test
    @Disabled("Success")
    void testTemplateSuccess() throws Exception {
        String inputString = "Location: CA, 1 book at 17.99, 1 potato chips at 3.99";


        MvcResult result = mockMvc.perform(get("/invoice/{inputString}", inputString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.TEXT_PLAIN_VALUE))
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString().contains("$17.99"));
        Assertions.assertThat(result.getResponse().getContentAsString().contains("$3.99"));
        Assertions.assertThat(result.getResponse().getContentAsString().contains("book"));
        Assertions.assertThat(result.getResponse().getContentAsString().contains("potato chips"));

        Assertions.assertThat(result.getResponse().getContentAsString().contains("subtotal"));
        Assertions.assertThat(result.getResponse().getContentAsString().contains("tax"));
        Assertions.assertThat(result.getResponse().getContentAsString().contains("total"));

    }


    @Test
    @Disabled("Location Data Not Found!")
    void testLocationNotFound() throws Exception {
        String inputString = "Location: TW, 1 book at 17.99, 1 potato chips at 3.99";

        mockMvc.perform(get("/invoice/{inputString}", inputString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof LocationDataNotFoundException))
                .andExpect(result -> assertEquals("Location Data Not Found!", result.getResolvedException().getMessage()));
    }


    @Test
    @Disabled("Input Data Format Error!")
    void testInputDataFormatError() throws Exception {
        String inputString = "Location: NY,1 book at 17.99, 1 potato chips at 3.99";

        mockMvc.perform(get("/invoice/{inputString}", inputString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RequestFormatException))
                .andExpect(result -> assertEquals("Input Data Format Error!", result.getResolvedException().getMessage()));
    }

}