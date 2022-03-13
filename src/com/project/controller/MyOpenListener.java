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
        String userDir = System.getProperty("user.home");
        JFileChooser fc = new JFileChooser(userDir + "/Desktop");

        if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getPath();


            String line;
            BufferedReader reader = null;
            InvoiceHeader invHeader;
            try {
                invTableModel.setRowCount(0);
                reader = new BufferedReader(new FileReader(path));
                while ((line = reader.readLine()) != null) {
                    invTableModel.insertRow(invTableModel.getRowCount(), line.split(","));
                }

                frame.setMainPath(path);
                reader.close();


                //get details csv
                File mainFile = fc.getSelectedFile();
                String detailsFilePath =
                        mainFile.getParent() + "\\" + mainFile.getName().substring(0, mainFile.getName().length() - 4) + "Details.csv";
                reader = new BufferedReader(new FileReader(detailsFilePath));

                invItemTableModel.setRowCount(0);
                invItemTableModel.setRowCount(1);
                while ((line = reader.readLine()) != null) {
                    invItemTableModel.insertRow(invItemTableModel.getRowCount()-1, line.split(","));
                }
                frame.setDetailsPath(detailsFilePath);

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
        calcInvTotal();
    }

    private void calcInvTotal(){
        for (int i = 0; i<invTableModel.getRowCount(); i++){
            int invTotalPrice = 0;
            for (int j = 0; j<invItemTableModel.getRowCount()-1; j++){
                String invItemNo = (String)invItemTable.getValueAt(j,0);
                String invNo = (String) invTable.getValueAt(i,0);

                if(Integer.parseInt(invItemNo) == Integer.parseInt(invNo)){
                    invTotalPrice += (int) invItemTable.getValueAt(j,4);
                }
            }
            invTable.setValueAt(invTotalPrice,i,3);
        }
    }
}


