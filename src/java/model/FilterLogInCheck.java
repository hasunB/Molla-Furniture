/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author hasun
 */
@WebFilter(urlPatterns = {"/myAccount.html"})
public class FilterLogInCheck implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServeletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServeletResponse = (HttpServletResponse) response;

        if (httpServeletRequest.getSession().getAttribute("user") != null) {
            chain.doFilter(request, response);
        } else {
            httpServeletResponse.sendRedirect("/Molla/");
        }

    }

    @Override
    public void destroy() {
    }

}
