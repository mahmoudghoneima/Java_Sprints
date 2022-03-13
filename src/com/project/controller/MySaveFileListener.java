package com.project.controller;

import com.project.view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MySaveFileListener implements ActionListener {
    MainFrame frame;

    public MySaveFileListener(MainFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        BufferedWriter csv = null;
        try {
            DefaultTableModel model = frame.getInvoicesTblModel();//get the header table model

            csv = new BufferedWriter(new FileWriter(frame.getMainPath()));//get the header file path saved from last load

            //loop over the whole table by a nested loop looping through rows and columns
            for (int i = 0; i < model.getRowCount() ; i++) {
                for (int j = 0; j < model.getColumnCount()-1; j++) {
                    //write the value of the cell selected followed by a comma to follow csv format
                    csv.write(frame.getInvoicesTbl().getValueAt(i, j).toString() + ",");
                }
                csv.write("\n");//write a new line in file every row
            }
            JOptionPane.showMessageDialog(frame,"File Saved!");//confirm to the user that the file is saved so the can safely exit


        } catch (IOException eIO) {
            eIO.printStackTrace();
        } finally {
            try {
                csv.close();
            } catch (IOException ex) {
            }
        }
    }
}
