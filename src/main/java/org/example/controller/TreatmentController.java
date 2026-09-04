package org.example.controller;

import com.google.gson.Gson;
import org.example.model.Treatment;
import org.example.repository.TreatmentRepository;
import org.example.service.TreatmentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DatabaseConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

@WebServlet("/treatments")
public class TreatmentController extends HttpServlet {

    private TreatmentService treatmentService;
    private Gson gson = new Gson();

    // Constructor for Dependency Injection (Used in our Tests)
    public TreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    // Default constructor required by Tomcat
    public TreatmentController() {
    }

    @Override
    public void init() throws ServletException {
        try {
            // Initialize the DB Connection, Repository, and Service
            Connection connection = DatabaseConnection.getConnection(); // මෙතන ඔයාගේ සැබෑ DB method එක යොදන්න
            TreatmentRepository repository = new TreatmentRepository(connection);
            this.treatmentService = new TreatmentService(repository);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize TreatmentController", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Set response type to JSON
        resp.setContentType("application/json");

        // Both Admin and Staff can access this. Filter already validates the JWT.
        List<Treatment> treatments = treatmentService.getAllTreatments();
        String jsonResponse = gson.toJson(treatments);

        // Send successful response
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = resp.getWriter();
        out.write(jsonResponse);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Step 1 & 2: RBAC Security Check
        String role = (String) req.getAttribute("role");
        if (role == null || !role.equals("Admin")) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return; // Stop execution if not Admin
        }

        // Step 3 & 4: Read Data and Deserialize using Gson
        BufferedReader reader = req.getReader();
        Treatment newTreatment = gson.fromJson(reader, Treatment.class);

        // Step 5 & 6: Service Execution & Response
        boolean isAdded = treatmentService.addTreatment(newTreatment);

        if (isAdded) {
            resp.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
        }
    }
}