package com.example.demoshoppingreceipt.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.regex.Pattern;

/**
 * @Description
 * @Name filter
 * @Author xd_to
 * @Project demoShoppingReceipt
 * @Package com.example.demoshoppingreceipt.config
 * @Date 2024/7/23
 * @Version 1.0
 */


@WebFilter(urlPatterns = "/*")
@Order(value = 1)
public class MyFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
        String decodePath = URLDecoder.decode(path, "UTF-8");
        String regex = "/(invoice)/.*";
        boolean allowedPath = Pattern.matches(regex,decodePath);
        if (allowedPath) {
            chain.doFilter(req, res);
        } else {
            PrintWriter writer = res.getWriter();
            writer.write("Function Not Found!");
            writer.flush();
        }
    }

    @Override
    public void destroy() {
    }

}
