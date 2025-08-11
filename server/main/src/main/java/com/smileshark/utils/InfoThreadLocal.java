package com.smileshark.utils;

import com.smileshark.entity.Customer;
import com.smileshark.entity.Customer;
import com.smileshark.entity.Merchant;
import com.smileshark.entity.Staff;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class InfoThreadLocal {
    // 用户的数据
    private static final ThreadLocal<Customer> localCustomer = new ThreadLocal<>();
    // 员工的数据
    private static final ThreadLocal<Staff> localStaff= new ThreadLocal<>();
    // 商户的数据
    private static final ThreadLocal<Merchant> localMerchant= new ThreadLocal<>();
    // 请求头和响应头
    private static final ThreadLocal<HttpServletRequest> request = new ThreadLocal<>();
    private static final ThreadLocal<HttpServletResponse> response = new ThreadLocal<>();
    // 身份验证是否通过
    private static final ThreadLocal<Boolean> pass=new ThreadLocal<>();

    public static void setCustomer(Customer customer) {
        localCustomer.set(customer);
    }

    public static Customer getCustomer() {
        return localCustomer.get();
    }

    public static void setStaff(Staff staff) {
        localStaff.set(staff);
    }
    public static Staff getStaff() {
        return localStaff.get();
    }

    public static void setMerchant(Merchant merchant) {
        localMerchant.set(merchant);
    }

    public static Merchant getMerchant() {
        return localMerchant.get();
    }

    public static void setPass(Boolean pass) {
        InfoThreadLocal.pass.set(pass);
    }

    public static Boolean getPass() {
        return pass.get();
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
