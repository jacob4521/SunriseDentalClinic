package org.example.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.example.model.Appointment;
import org.example.service.AppointmentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Time;
import java.util.List;

@WebServlet("/appointments")
public class AppointmentServlet extends HttpServlet {

    private AppointmentService appointmentService = new AppointmentService();
    private Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .registerTypeAdapter(Time.class, (JsonDeserializer<Time>) (json, typeOfT, context) -> Time.valueOf(json.getAsString()))
            .create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String role = (String) req.getAttribute("role");

        if (role == null) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Status
            resp.getWriter().write("Forbidden: Access Denied");
            return;
        }

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        Appointment appointment = gson.fromJson(sb.toString(), Appointment.class);

        boolean isAdded = appointmentService.addAppointment(appointment);

        if (isAdded) {
            resp.setStatus(HttpServletResponse.SC_CREATED); // 201 Status
            resp.getWriter().write("Appointment created successfully");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Status
            resp.getWriter().write("Failed to create appointment");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String role = (String) req.getAttribute("role");
        if (role == null) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("Forbidden: Access Denied");
            return;
        }

        resp.setContentType("application/json");
        String idParam = req.getParameter("id");

        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                Map<String, Object> details = appointmentService.getAppointmentWithPatientDetails(id);

                if (details != null) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write(gson.toJson(details));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                    resp.getWriter().write("{\"error\": \"Appointment not found\"}");
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Invalid ID format\"}");
            }
        }
        else {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(appointments));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String role = (String) req.getAttribute("role");

        // Admin සහ Staff අයට පමණක් Delete කිරීමට අවසර ඇත
        if (role == null || (!role.equals("Admin") && !role.equals("Staff"))) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("Forbidden: Only Admin and Staff can delete appointments");
            return;
        }

        // URL එකේ එන ?id= අගය ලබා ගැනීම (උදා: /appointments?id=1)
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Missing appointment ID");
            return;
        }

        int id = Integer.parseInt(idParam);
        boolean isDeleted = appointmentService.deleteAppointment(id);

        if (isDeleted) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Appointment deleted successfully");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Failed to delete appointment");
        }
    }
}