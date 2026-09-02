package org.example.controller;

import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServletTest {

    @Mock
    private HttpServletRequest req;

    @Mock
    private HttpServletResponse resp;

    @Mock
    private UserService userService;

    private UserServlet userServlet;

    @BeforeEach
    void setUp() {
        userServlet = new UserServlet(userService);
    }

    @Test
    void doPost_registersUserSuccessfully() throws Exception {
        // Arrange
        when(req.getPathInfo()).thenReturn("/register");

        String json = "{\"username\":\"testUser\",\"password\":\"password123\",\"role\":\"Staff\"}";
        BufferedReader reader = new BufferedReader(new StringReader(json));
        when(req.getReader()).thenReturn(reader);

        when(userService.registerUser(any())).thenReturn(true);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(resp.getWriter()).thenReturn(pw);

        // Act
        userServlet.doPost(req, resp);

        // Assert: Expect created (201)
        verify(resp).setStatus(HttpServletResponse.SC_CREATED);
    }
}
