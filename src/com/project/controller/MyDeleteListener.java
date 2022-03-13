package com.project.controller;

import com.project.view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyDeleteListener implements ActionListener {
    DefaultTableModel invoicesTblModel;
    JTable invoicesTbl;
    MainFrame frame;
    public MyDeleteListener(MainFrame frame) {
        this.frame = frame;
        this.invoicesTblModel = frame.getInvoicesTblModel();
        this.invoicesTbl = frame.getInvoicesTbl();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedRow = invoicesTbl.getSelectedRow();
        removeInvoiceItems(selectedRow);
        invoicesTblModel.removeRow(selectedRow);

    }

    private void removeInvoiceItems(int row){
        String invNo = (String) invoicesTbl.getValueAt(row,0);
        System.out.println(invNo);
        for(int i = 0; i<frame.getInvItemTblModel().getRowCount()-1; i++){
            String invItemNo = (String)frame.getInvItemTable().getValueAt(i,0);


            if(Integer.parseInt(invItemNo) == Integer.parseInt(invNo)){
                frame.getInvItemTblModel().removeRow(i);
                i--;
            }
        }
    }
}
