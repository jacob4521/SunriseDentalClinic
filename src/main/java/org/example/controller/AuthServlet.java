package org.example.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.User;
import org.example.service.UserService;
import org.example.util.JwtUtil;
import com.google.gson.Gson;

import java.io.IOException;

@WebServlet("/auth/login")
public class AuthServlet extends HttpServlet {
    
    public AuthServlet() {
        this(new UserService(new org.example.repository.UserRepository()));
    }

    private final UserService userService;

    public AuthServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        Gson gson = new Gson();

        // Parse incoming JSON into User object to extract username and password
        User credentials = gson.fromJson(req.getReader(), User.class);

        // Authenticate user using UserService
        User authenticatedUser = userService.authenticateUser(credentials.getUsername(), credentials.getPassword());

        // If authentication is successful
        if (authenticatedUser != null) {
            // Get user's role from authenticated user
            String role = authenticatedUser.getRole();

            // Generate JWT token
            String token = JwtUtil.generateToken(credentials.getUsername(), role);

            // Return token in JSON response with 200 OK status
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"token\": \"" + token + "\", \"role\": \"" + role + "\"}");
        } else {
            // Authentication failed - return 401 Unauthorized
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Invalid username or password\"}");
        }
    }
}
