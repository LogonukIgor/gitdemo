package by.logonuk.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "AuthHeaderFilter")
public class AuthHeaderFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        chain.doFilter(request, response);
        System.out.println("In Auth filter!");

        HttpServletRequest castedRequest = (HttpServletRequest) request;
        //String authHeader = castedRequest.getHeader("X-Auth-Token");
        String authHeader = castedRequest.getHeader("Key");
        if (StringUtils.isNotBlank(authHeader)) { //for future JWT Token Auth
            System.out.println("Header was found with payload: " + authHeader);
        } else {
            System.out.println("Header was not found!");
        }

        chain.doFilter(request, response);
    }
}
