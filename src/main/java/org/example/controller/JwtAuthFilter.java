package org.example.controller;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.util.JwtUtil;

import java.io.IOException;

@WebFilter("/*")
public class JwtAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        if (path.endsWith("/auth/login")) {
            chain.doFilter(request, response);
            return;
        }

        // For all other endpoints (like /dentists), validate JWT token
        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.setContentType("application/json");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\": \"Missing or invalid Authorization header\"}");
            return;
        }

        try {
            String token = authHeader.substring(7);

            // Extract role from token
            String role = JwtUtil.extractRole(token);

            httpRequest.setAttribute("role", role);


        } catch (Exception e) {
            e.printStackTrace();

            httpResponse.setContentType("application/json");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\": \"Invalid or expired token\"}");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(jakarta.servlet.FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}