package com.example.demoshoppingreceipt.controller;

import com.example.demoshoppingreceipt.Exception.CustomizationException;
import com.example.demoshoppingreceipt.Exception.LocationDataNotFoundException;
import com.example.demoshoppingreceipt.Exception.RequestFormatException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.tartarus.snowball.ext.PorterStemmer;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Description
 * @Name InvoiceController
 * @Author t0nn
 * @Project demoShoppingReceipt
 * @Package com.example.controller
 * @Date 2024/7/21
 * @Version 1.0
 */
@RestController
@Configuration
@PropertySource(value = "classpath:rates.properties")
public class InvoiceController {
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

    @GetMapping("/invoice/{inputString}")
    public void calcInvoice(HttpServletResponse response, @PathVariable("inputString") String inputString) {



        String regex = "(Location:)\\s[a-zA-Z]*((,)\\s[0-9]*\\s[a-zA-Z\\s]*\\s(at)\\s[0-9.]*)*";
        if (!Pattern.matches(regex, inputString)) {
            throw new RequestFormatException("Input Data Format Error!");
        }

        String[] DataArray = inputString.toLowerCase().split(",");
        String Location = DataArray[0].split(":")[1].trim();


        if (!list.contains(Location)) {
            throw new LocationDataNotFoundException("Location Data Not Found!");
        }



        List<Double> subtotalList = new ArrayList<>();
        List<Double> taxList = new ArrayList<>();

        boolean flag = true;
        String item;
        Integer qty;
        Double price;

        try {

            response.setContentType("text/plain");
            response.getWriter().write(String.format("%-20s", "item") + String.format("%8s", "price") + String.format("%16s", "qty") + "\n");
            response.getWriter().write("\n");

            for (String detail : DataArray) {
                if (flag) {
                    flag = false;
                    continue;
                }

                price = Double.parseDouble(detail.split(" at ")[1].trim());
                qty = Integer.parseInt(detail.split(" at ")[0].replaceAll("\\D", ""));
                item =  pluralizeChange(detail.split(" at ")[0].replace(qty.toString(), "").trim());
                response.getWriter().write(String.format("%-20s",item) + String.format("%8s", "$" + price.toString()) + String.format("%16s", qty.toString()) + "\n");

                if (Location.equals("ca")) {
                    subtotalList.add(price * qty);
                    if (!ca_exempt.contains(item))
                        taxList.add(price * qty * ca_tex);
                } else if (Location.equals("ny")) {
                    subtotalList.add(price * qty);
                    if (!ny_exempt.contains(item))
                        taxList.add(price * qty * ny_tex);
                }
            }

            String sub_string = formatToNumber(BigDecimal.valueOf(subtotalList.stream().mapToDouble(Double::doubleValue).sum()));
            String tax_string = formatToNumber(roundup(taxList.stream().mapToDouble(Double::doubleValue).sum()));
            String tot_string = formatToNumber(new BigDecimal(sub_string).add(new BigDecimal(tax_string)));

            response.getWriter().write(String.format("%-20s", "subtotal:") + String.format("%9s", "") + String.format("%16s", "$" + sub_string + "\n"));
            response.getWriter().write(String.format("%-20s", "tax:") + String.format("%9s", "") + String.format("%16s", "$" + tax_string + "\n"));
            response.getWriter().write(String.format("%-20s", "total:") + String.format("%9s", "") + String.format("%16s", "$" + tot_string + "\n"));
        } catch (IOException ex) {
            throw new CustomizationException("Internal Error!");
        }
    }


    private BigDecimal roundup(Double input) {
        BigDecimal base = BigDecimal.valueOf(Math.ceil(input / 0.05));
        return base.multiply(new BigDecimal(0.05));
    }

    public String formatToNumber(BigDecimal obj) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (obj.compareTo(BigDecimal.ZERO) == 0) {
            return "0.00";
        } else if (obj.compareTo(BigDecimal.ZERO) > 0 && obj.compareTo(new BigDecimal(1)) < 0) {
            return "0" + df.format(obj).toString();
        } else {
            return df.format(obj).toString();
        }
    }

    public String pluralizeChange(String word) {
        PorterStemmer stemmer = new PorterStemmer();
        stemmer.setCurrent(word);
        stemmer.stem();
        return stemmer.getCurrent();
    }
}
