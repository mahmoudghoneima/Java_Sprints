package com.project.controller;

import com.project.view.MainFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MyCancelListener implements ActionListener {
    MainFrame frame;

    public MyCancelListener(MainFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {


        BufferedReader reader = null;
        String line;
        try {
            reader = new BufferedReader(new FileReader(frame.getDetailsPath()));
            frame.getInvItemTblModel().setRowCount(0);
            frame.getInvItemTblModel().setRowCount(1);
            while ((line = reader.readLine()) != null) {
                frame.getInvItemTblModel().insertRow(frame.getInvItemTblModel().getRowCount() - 1, line.split(","));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
            }
        }
        clearPanel();

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
}
