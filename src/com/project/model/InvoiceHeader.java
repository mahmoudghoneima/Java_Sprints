package com.project.model;

import java.util.Date;

public class InvoiceHeader {
    private int invoiceNum;
    private String customerName;
    private Date invoiceDate;
    private InvoiceLine[] invoiceLines;

    public InvoiceHeader(int invoiceNum, String customerName, Date invoiceDate) {
        this.invoiceNum = invoiceNum;
        this.customerName = customerName;
        this.invoiceDate = invoiceDate;
    }
}
