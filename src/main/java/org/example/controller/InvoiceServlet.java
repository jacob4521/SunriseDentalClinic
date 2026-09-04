package org.example.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Invoice;
import org.example.service.InvoiceService;


import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/invoices")
public class InvoiceServlet extends HttpServlet {

    private InvoiceService invoiceService;
    private Gson gson = new Gson();

    // Tomcat මඟින් ධාවනය කිරීම සඳහා
    public InvoiceServlet() {
        this.invoiceService = new InvoiceService();
    }

    // Testing (Mocking) සඳහා
    public InvoiceServlet(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        Invoice invoice = gson.fromJson(sb.toString(), Invoice.class);

        // අලුතින් එකතු කළ Validation එක: Invoice එක null නම් හෝ appointmentId එක 0 ට අඩු නම්
        if (invoice == null || invoice.getAppointmentId() <= 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid or empty JSON payload\"}");
            return;
        }

        boolean isCreated = invoiceService.addInvoice(invoice);

        if (isCreated) {
            resp.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
            resp.getWriter().write("{\"message\": \"Invoice created successfully\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            resp.getWriter().write("{\"error\": \"Failed to create invoice. Please check if the Appointment ID exists.\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String idParam = req.getParameter("id");

        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                Invoice invoice = invoiceService.getInvoiceById(id);

                if (invoice != null) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write(gson.toJson(invoice));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{\"error\": \"Invoice not found\"}");
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Invalid Invoice ID format\"}");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invoice ID is required\"}");
        }
    }
}