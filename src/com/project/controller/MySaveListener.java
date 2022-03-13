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
            if (frame.isNew()) {//check if the invoice is new because otherwise invoice date text field would be disabled
                Date date;

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");//set date format
                sdf.setLenient(false);//setting the format not to be lenient for concise date entry
                date = sdf.parse(frame.getInvDateTxtField().getText());//attempt to parse the entered date to the previous format
                frame.getInvoicesTblModel().addRow(new Object[]{
                        Integer.toString(frame.getNoOfInvoices()), frame.getInvDateTxtField().getText(),
                        frame.getCustomerNameTxtField().getText(), frame.getInvTotal()});//add a new row to the header table with th new invoice's data

            }
            BufferedWriter csv = null;
            try {
                DefaultTableModel model = frame.getInvItemTblModel();//get item table model

                csv = new BufferedWriter(new FileWriter(frame.getDetailsPath()));//starting the buffered writer removes all previous data in file

                //loop over table using a nested loop that loops over rows and columns
                for (int i = 0; i < model.getRowCount() -1; i++) {
                    for (int j = 0; j < model.getColumnCount()-1; j++) {
                        //write each cell's data followed by a comma to follow csv format
                        csv.write(frame.getInvItemTable().getValueAt(i, j).toString() + ",");
                    }
                    csv.write("\n");//write a new line in the file every new row
                }


            } catch (IOException eIO) {
                eIO.printStackTrace();
            } finally {
                try {
                    csv.close();
                } catch (IOException ex) {
                }
            }

            clearPanel();//set invoice data block (top right) to default
            calcInvTotal();//calculate each invoice's total

        } catch (ParseException ex) {
            //exception is raised when date parsing isn't completed properly;
            // keep in mind that the parsing section of the code is at the beginning to prevent any changes to happen
            // until date is entered correctly

            //inform user that the date entered is incorrect
            JOptionPane.showMessageDialog(
                    frame, "Date format incorrect. Please change to save", "Invalid Date", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearPanel() {
        //set invoice data block (top right) to default
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
        for (int i = 0; i < invTableModel.getRowCount(); i++) {//loop over header table
            int invTotalPrice = 0;//set sum to zero every new invoice iteration
            for (int j = 0; j < invItemTableModel.getRowCount() - 1; j++) {
                String invNo = (String) invTable.getValueAt(i, 0);//get the invoice no. value from header table

                //try and catch used here due to some data comes from file as string, while new data entered by the user might be a number
                try {
                    String invItemNo = (String) invItemTable.getValueAt(j, 0);//get the item's invoice no.

                    if (Integer.parseInt(invItemNo) == Integer.parseInt(invNo)) {//check if the item's invoice number is the same as the invoice selected
                        invTotalPrice += (int) invItemTable.getValueAt(j, 4);//add item price to sum variable
                    }
                } catch (Exception e) {
                    //same code as in previous catch block just different casting
                    int invItemNo = (int) invItemTable.getValueAt(j, 0);

                    if (invItemNo == Integer.parseInt(invNo)) {
                        invTotalPrice += (int) invItemTable.getValueAt(j, 4);
                    }
                }
            }
            invTable.setValueAt(invTotalPrice, i, 3);//set invoice total value in header table
        }
    }
}
