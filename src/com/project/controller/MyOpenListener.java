package com.project.controller;


import com.project.model.InvoiceHeader;
import com.project.view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Path;

import com.project.view.MainFrame;

public class MyOpenListener implements ActionListener {
    MainFrame frame;
    DefaultTableModel invTableModel;
    DefaultTableModel invItemTableModel;
    JTable invTable;
    JTable invItemTable;


    public MyOpenListener(MainFrame frame, DefaultTableModel tbl) {
        this.frame = frame;
        this.invTableModel = frame.getInvoicesTblModel();
        this.invItemTableModel = frame.getInvItemTblModel();
        this.invTable = frame.getInvoicesTbl();
        this.invItemTable = frame.getInvItemTable();
    }

    public void actionPerformed(ActionEvent ae) {
        //create file chooser and let it start at desktop
        String userDir = System.getProperty("user.home");
        JFileChooser fc = new JFileChooser(userDir + "/Desktop");

        if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getPath();//get the selected file path from the file chooser


            String line;
            BufferedReader reader = null;
            InvoiceHeader invHeader;
            try {
                //set row count of the header table to 0 to add data properly
                invTableModel.setRowCount(0);
                reader = new BufferedReader(new FileReader(path));
                while ((line = reader.readLine()) != null) {//read file line by line
                    invTableModel.insertRow(invTableModel.getRowCount(), line.split(","));//insert each line as a table row
                }

                frame.setMainPath(path);//send the path of the header file to frame for later use
                reader.close();// close reader



                File mainFile = fc.getSelectedFile();//get header file
                //get the details file by manipulating the header file's name
                String detailsFilePath =
                        mainFile.getParent() + "\\" + mainFile.getName().substring(0, mainFile.getName().length() - 4) + "Details.csv";
                reader = new BufferedReader(new FileReader(detailsFilePath));

                invItemTableModel.setRowCount(0);//set row count to 0 to remove all previous data or rows
                invItemTableModel.setRowCount(1);//set invoice line table row count to 1 to let user add lines
                while ((line = reader.readLine()) != null) {//read file line by line
                    invItemTableModel.insertRow(invItemTableModel.getRowCount()-1, line.split(","));//insert each line as a table row
                }
                frame.setDetailsPath(detailsFilePath);//send path of the lines file to frame for later use

                //get the last invoice's number to know what number will the next invoice created be
                String lastInvoiceNumber = (String) invTableModel.getValueAt(invTableModel.getRowCount()-1, 0);
                frame.setNoOfInvoices(Integer.parseInt(lastInvoiceNumber));

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "File not found please try a different file");
                e.printStackTrace();

            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }


        }
        calcInvTotal();//calculate total price of each invoice line from lines file because csv file provided doesn't contain it
    }

    private void calcInvTotal(){
        for (int i = 0; i<invTableModel.getRowCount(); i++){//loop over header table
            int invTotalPrice = 0;//set the total to zero when iterating to a new invoice
            for (int j = 0; j<invItemTableModel.getRowCount()-1; j++){//loop over item table
                String invItemNo = (String)invItemTable.getValueAt(j,0);// get invoice no. from item table
                String invNo = (String) invTable.getValueAt(i,0);// get invoice no. from header table

                if(Integer.parseInt(invItemNo) == Integer.parseInt(invNo)){//if the item has the same invoice no. as the invoice selected
                    invTotalPrice += (int) invItemTable.getValueAt(j,4);// add total price to total
                }
            }
            invTable.setValueAt(invTotalPrice,i,3);//set total price in header table
        }
    }
}


