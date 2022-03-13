package com.project.controller;

import com.project.view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MySaveListener implements ActionListener {
    MainFrame frame;

    public MySaveListener(MainFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            if (frame.isNew()) {
                Date date;

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
                sdf.setLenient(false);
                date = sdf.parse(frame.getInvDateTxtField().getText());
                frame.getInvoicesTblModel().addRow(new Object[]{
                        Integer.toString(frame.getNoOfInvoices()), frame.getInvDateTxtField().getText(),
                        frame.getCustomerNameTxtField().getText(), frame.getInvTotal()});

            }
            BufferedWriter csv = null;
            try {
                DefaultTableModel model = frame.getInvItemTblModel();

                csv = new BufferedWriter(new FileWriter(frame.getDetailsPath()));


                for (int i = 0; i < model.getRowCount() -1; i++) {
                    for (int j = 0; j < model.getColumnCount()-1; j++) {
                        csv.write(frame.getInvItemTable().getValueAt(i, j).toString() + ",");
                    }
                    csv.write("\n");
                }


            } catch (IOException eIO) {
                eIO.printStackTrace();
            } finally {
                try {
                    csv.close();
                } catch (IOException ex) {
                }
            }

            clearPanel();
            calcInvTotal();

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(
                    frame, "Date format incorrect. Please change to save", "Invalid Date", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearPanel() {
        frame.getInvDateTxtField().setText("");
        frame.getInvDateTxtField().setEnabled(false);
        frame.getCustomerNameTxtField().setText("");
        frame.getCustomerNameTxtField().setEnabled(false);
        frame.setInvTotal(0);
        frame.setNew(false);
        frame.getInvTotalLbl().setText("  Invoice Total         " + frame.getInvTotal());
    }

    private void calcInvTotal() {
        DefaultTableModel invTableModel = frame.getInvoicesTblModel();
        DefaultTableModel invItemTableModel = frame.getInvItemTblModel();
        JTable invTable = frame.getInvoicesTbl();
        JTable invItemTable = frame.getInvItemTable();
        for (int i = 0; i < invTableModel.getRowCount(); i++) {
            int invTotalPrice = 0;
            for (int j = 0; j < invItemTableModel.getRowCount() - 1; j++) {
                String invNo = (String) invTable.getValueAt(i, 0);

                try {
                    String invItemNo = (String) invItemTable.getValueAt(j, 0);

                    if (Integer.parseInt(invItemNo) == Integer.parseInt(invNo)) {
                        invTotalPrice += (int) invItemTable.getValueAt(j, 4);
                    }
                } catch (Exception e) {
                    int invItemNo = (int) invItemTable.getValueAt(j, 0);

                    if (invItemNo == Integer.parseInt(invNo)) {
                        invTotalPrice += (int) invItemTable.getValueAt(j, 4);
                    }
                }
            }
            invTable.setValueAt(invTotalPrice, i, 3);
        }
    }
}
