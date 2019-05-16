package com.liuqi.tools.codelife.config;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 将前缀api去掉
 *
 * @author LiuQI 2019/5/16 10:19
 * @version V1.0
 **/
@Component
@WebFilter(filterName = "apiProcessFilter", urlPatterns = "/*")
@Order(1)
public class ApiProcessFilter extends OncePerRequestFilter {

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 去掉如api前缀
        String path = request.getRequestURI().replaceFirst("/api", "");
        request.getRequestDispatcher(path).forward(request, response);
    }
}
