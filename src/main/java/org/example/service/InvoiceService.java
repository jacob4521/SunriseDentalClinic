package org.example.service;

import org.example.model.Invoice;
import org.example.repository.InvoiceRepository;

public class InvoiceService {

    private InvoiceRepository invoiceRepository;

    public InvoiceService() {
        this.invoiceRepository = new InvoiceRepository();
    }

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public boolean addInvoice(Invoice invoice) {
        return invoiceRepository.addInvoice(invoice);
    }

    public Invoice getInvoiceById(int invoiceId) {
        return invoiceRepository.getInvoiceById(invoiceId);
    }
}