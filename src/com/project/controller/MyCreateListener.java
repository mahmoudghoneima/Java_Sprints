package com.project.controller;

import com.project.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyCreateListener implements ActionListener {
    MainFrame frame;

    public MyCreateListener(MainFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        frame.getNoOfInvoicesLbl().setText("Invoice Number         " + frame.incrementNoOfInvoices());//add no. of invoices to gui incremented
        frame.setNew(true);//tell code that a new invoice is being created
        //enable text fields
        frame.getCustomerNameTxtField().setEnabled(true);
        frame.getInvDateTxtField().setEnabled(true);
        //tell user where to enter new invoice's data
        JOptionPane.showMessageDialog(frame,"Enter the new invoice's data in the right panel(Check Tooltip)");
    }
}
