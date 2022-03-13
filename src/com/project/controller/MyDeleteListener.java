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
        int selectedRow = invoicesTbl.getSelectedRow();//get the invoice the user wants to delete
        removeInvoiceItems(selectedRow);
        invoicesTblModel.removeRow(selectedRow);//remove the selected row

    }

    private void removeInvoiceItems(int row){
        String invNo = (String) invoicesTbl.getValueAt(row,0);//get invoice no.
        for(int i = 0; i<frame.getInvItemTblModel().getRowCount()-1; i++){//loop over invoice items table
            String invItemNo = (String)frame.getInvItemTable().getValueAt(i,0);//get invoice no. for each item

            if(Integer.parseInt(invItemNo) == Integer.parseInt(invNo)){//check if it is the same as the selected invoice
                frame.getInvItemTblModel().removeRow(i);//remove the row from the invoice items table
                i--;//decrease a loop iteration every time a row is deleted to prevent null pointer exceptions
            }
        }
    }
}
