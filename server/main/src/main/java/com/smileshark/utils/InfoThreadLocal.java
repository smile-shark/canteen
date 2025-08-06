package com.smileshark.utils;

import com.smileshark.entity.Customer;
import com.smileshark.entity.Customer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class InfoThreadLocal {
    private static final ThreadLocal<Customer> localCustomer = new ThreadLocal<>();
    private static final ThreadLocal<HttpServletRequest> request = new ThreadLocal<>();
    private static final ThreadLocal<HttpServletResponse> response = new ThreadLocal<>();

    public static void setCustomer(Customer customer) {
        localCustomer.set(customer);
    }

    public static Customer getCustomer() {
        return localCustomer.get();
    }

    public static void setRequest(HttpServletRequest request) {
        InfoThreadLocal.request.set(request);
    }

    public static HttpServletRequest getRequest() {
        return request.get();
    }

    public static void setResponse(HttpServletResponse response) {
        InfoThreadLocal.response.set(response);
    }

    public static HttpServletResponse getResponse() {
        return response.get();
    }
}
