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
        if ("/register".equals(path)) {
            Gson gson = new Gson();
            User user = gson.fromJson(req.getReader(), User.class);
            boolean registered = userService.registerUser(user);
            if (registered) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write("{\"message\": \"User registered successfully\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().write("{\"error\": \"User could not be registered\"}");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}