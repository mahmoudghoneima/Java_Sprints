package com.project.view;

import com.project.view.MainFrame;

import javax.swing.*;

public class InvoiceRunner {
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();//create an instance of the frame that the user will use

        //windows look and feel (comment of not needed)
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        mainFrame.setVisible(true);// make the frame visible to the user
    }
}
