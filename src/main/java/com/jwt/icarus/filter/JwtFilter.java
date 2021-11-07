package com.jwt.icarus.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.jwt.icarus.values.Values.*;

@Slf4j
@WebFilter("/*")
@Component
public class JwtFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String format = StringUtils.defaultIfEmpty(request.getParameter("format"), DEFAULT_FORMAT);
        String uri = request.getRequestURI();
        String method = request.getMethod();

        log.info("JWT Request URI : " + uri);
        log.info("JWT Request Remote Host : " + request.getRemoteAddr());
        log.info("Jwt Request format Type : " + format);

        if (MAPPING_INFO.get(uri) != null) {
            if (HttpMethod.POST.matches(method)) {
                if (MEDIA_TYPES.get(format) != null) {
                    chain.doFilter(request, response);
                } else {
                    setErrorAttribute(request, new UnavailableException("Could not acceptable format type"));
                }
            } else {
                setErrorAttribute(request, new HttpRequestMethodNotSupportedException("Request method 'GET' not supported"));
            }
        } else {
            setErrorAttribute(request, new ServletException("Request path not found"));
        }
    }

    private void setErrorAttribute(HttpServletRequest request, Exception e) throws UnavailableException, HttpRequestMethodNotSupportedException, ServletException {
        request.setAttribute("ERROR_MESSAGE", e.getMessage());
        request.setAttribute("ERROR_REQUEST_URI", "/error");

        if (e instanceof UnavailableException) {
            throw (UnavailableException) e;
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            throw (HttpRequestMethodNotSupportedException) e;
        } else if (e instanceof ServletException) {
            throw (ServletException) e;
        }
    }

}
