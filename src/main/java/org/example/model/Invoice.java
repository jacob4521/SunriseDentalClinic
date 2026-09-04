package org.example.model;

public class Invoice {
    private int invoiceId;
    private int appointmentId;
    private double totalAmount;
    private String issuedDate;
    private String paymentStatus;

    public Invoice() {
    }

    public Invoice(int invoiceId, int appointmentId, double totalAmount, String issuedDate, String paymentStatus) {
        this.invoiceId = invoiceId;
        this.appointmentId = appointmentId;
        this.totalAmount = totalAmount;
        this.issuedDate = issuedDate;
        this.paymentStatus = paymentStatus;
    }

    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getIssuedDate() { return issuedDate; }
    public void setIssuedDate(String issuedDate) { this.issuedDate = issuedDate; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}