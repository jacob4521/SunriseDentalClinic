package org.example.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.UserService;
import org.example.User;
import com.google.gson.Gson;

import java.io.IOException;

public class UserServlet extends HttpServlet {
    private final UserService userService;

    public UserServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();
        Gson gson = new Gson();
        if ("/register".equals(path)) {
            User user = gson.fromJson(req.getReader(), User.class);
            boolean registered = userService.registerUser(user);
            if (registered) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write("{\"message\": \"User registered successfully\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().write("{\"error\": \"User could not be registered\"}");
            }
        } else if ("/login".equals(path)) {
            User credentials = gson.fromJson(req.getReader(), User.class);
            User authenticated = userService.authenticateUser(credentials.getUsername(), credentials.getPassword());
            if (authenticated != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(gson.toJson(authenticated));
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("{\"error\": \"Invalid credentials\"}");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}