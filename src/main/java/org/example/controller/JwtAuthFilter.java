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
import com.google.gson.Gson;

import java.io.IOException;

@WebFilter("/users/*")
public class JwtAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String httpMethod = httpRequest.getMethod();

        // Allow POST requests to pass through without authentication
        if ("POST".equalsIgnoreCase(httpMethod)) {
            chain.doFilter(request, response);
            return;
        }

        // For other methods, validate JWT token
        String authHeader = httpRequest.getHeader("Authorization");

        // Check if Authorization header exists and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.setContentType("application/json");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\": \"Missing or invalid Authorization header\"}");
            return;
        }

        try {
            // Extract token string by removing "Bearer " prefix
            String token = authHeader.substring(7);

            // Extract role from token
            String userRole = JwtUtil.extractRole(token);

            // Set the role as a request attribute
            httpRequest.setAttribute("userRole", userRole);

            // Continue with the filter chain
            chain.doFilter(request, response);

        } catch (Exception e) {
            // Return 401 if token extraction or validation fails
            httpResponse.setContentType("application/json");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\": \"Invalid or expired token\"}");
        }
    }

    @Override
    public void init(jakarta.servlet.FilterConfig filterConfig) throws ServletException {
        // Filter initialization logic (if needed)
    }

    @Override
    public void destroy() {
        // Filter cleanup logic (if needed)
    }
}
