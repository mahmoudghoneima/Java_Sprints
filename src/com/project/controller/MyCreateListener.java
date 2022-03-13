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
        frame.getNoOfInvoicesLbl().setText("Invoice Number         " + frame.incrementNoOfInvoices());
        frame.setNew(true);
        frame.getCustomerNameTxtField().setEnabled(true);
        frame.getInvDateTxtField().setEnabled(true);
        JOptionPane.showMessageDialog(frame,"Enter the new invoice's data in the right panel(Check Tooltip)");
    }
}
