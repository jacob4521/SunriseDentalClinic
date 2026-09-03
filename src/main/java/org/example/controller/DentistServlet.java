package org.example.controller;

import com.google.gson.Gson;
import org.example.model.Dentist;
import org.example.service.DentistService;
import org.example.repository.DentistRepository;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DatabaseConnection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/dentists/*")
public class DentistServlet extends HttpServlet {

    private final DentistService dentistService;
    private final Gson gson = new Gson();

    // Default constructor for Tomcat
    public DentistServlet() throws SQLException {
        this.dentistService = new DentistService(new DentistRepository(DatabaseConnection.getConnection()));
    }

    // Constructor for testing
    public DentistServlet(DentistService dentistService) {
        this.dentistService = dentistService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        List<Dentist> dentists = dentistService.getAllDentists();
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(gson.toJson(dentists));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        // RBAC Security Check: Only Admins can add dentists
        String role = (String) req.getAttribute("role");
        if (role == null || !role.equalsIgnoreCase("Admin")) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("{\"error\": \"Access denied. Admin role required\"}");
            return;
        }

        Dentist dentist = gson.fromJson(req.getReader(), Dentist.class);
        boolean added = dentistService.addDentist(dentist);

        if (added) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\": \"Dentist added successfully\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Failed to add dentist\"}");
        }
    }
}