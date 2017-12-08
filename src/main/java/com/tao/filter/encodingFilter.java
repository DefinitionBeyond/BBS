package com.tao.filter;

import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import java.io.IOException;


/**
 * 设置过滤器
 * <p>
 * 所有编码格式都为uutf-8
 */

@Configuration
public class encodingFilter implements Filter {

    private String encode = "";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encode = filterConfig.getInitParameter("encode");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("utf-8");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
