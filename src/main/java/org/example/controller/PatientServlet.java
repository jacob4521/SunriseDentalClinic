package org.example.controller;

import com.google.gson.Gson;
import org.example.model.Patient;
import org.example.service.PatientService;
import org.example.repository.PatientRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class PatientServlet extends HttpServlet {

    private final PatientService patientService;
    private final Gson gson = new Gson();

    // Default constructor for actual API usage
    public PatientServlet() {
        this.patientService = new PatientService(new PatientRepository());
    }

    // Constructor for Mockito Testing
    public PatientServlet(PatientService patientService) {
        this.patientService = patientService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String role = (String) req.getAttribute("userRole");
        if (role == null) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Patient patient = gson.fromJson(req.getReader(), Patient.class);
        if (patientService.addPatient(patient)) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String role = (String) req.getAttribute("userRole");
        if (role == null) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<Patient> patients = patientService.getAllPatients();
            resp.getWriter().write(gson.toJson(patients));
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                Patient patient = patientService.getPatientById(id);
                if (patient != null) {
                    resp.getWriter().write(gson.toJson(patient));
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String role = (String) req.getAttribute("userRole");
        if (role == null) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Patient patient = gson.fromJson(req.getReader(), Patient.class);
        if (patientService.updatePatient(patient)) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String role = (String) req.getAttribute("userRole");
        // Only Admin can delete
        if (role == null || !role.equals("Admin")) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                if (patientService.deletePatient(id)) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}