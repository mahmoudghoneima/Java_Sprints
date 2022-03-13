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

        //initialize file reader and String variable that will contain file lines before try-catch
        BufferedReader reader = null;
        String line;
        try {
            //read from the file opened when the load menu option was used
            reader = new BufferedReader(new FileReader(frame.getDetailsPath()));

            frame.getInvItemTblModel().setRowCount(0);//set row count to zero to delete all previous data
            frame.getInvItemTblModel().setRowCount(1);//set it to one so user has room to add rows

            //read file line by line
            while ((line = reader.readLine()) != null) {
                //add each line as a row to table
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
        //set all invoice data(top right corner) to default values
        frame.getInvDateTxtField().setText("");
        frame.getInvDateTxtField().setEnabled(false);
        frame.getCustomerNameTxtField().setText("");
        frame.getCustomerNameTxtField().setEnabled(false);
        frame.setInvTotal(0);
        frame.setNew(false);
        frame.getInvTotalLbl().setText("  Invoice Total         " + frame.getInvTotal());
    }
}
