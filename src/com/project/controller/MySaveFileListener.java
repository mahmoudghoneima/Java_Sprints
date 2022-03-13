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
            DefaultTableModel model = frame.getInvoicesTblModel();

            csv = new BufferedWriter(new FileWriter(frame.getMainPath()));


            for (int i = 0; i < model.getRowCount() ; i++) {
                for (int j = 0; j < model.getColumnCount()-1; j++) {
                    csv.write(frame.getInvoicesTbl().getValueAt(i, j).toString() + ",");
                }
                csv.write("\n");
            }
            JOptionPane.showMessageDialog(frame,"File Saved!");


        } catch (IOException eIO) {
            System.out.println("hi");
            eIO.printStackTrace();
        } finally {
            try {
                csv.close();
            } catch (IOException ex) {
            }
        }
    }
}
