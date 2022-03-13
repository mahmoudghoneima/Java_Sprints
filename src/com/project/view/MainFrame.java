package com.project.view;


import com.project.controller.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.nio.file.Path;

public class MainFrame extends JFrame {
    private int noOfInvoices;
    private int invTotal;
    private JTable invoicesTbl;
    private JTable invItemTable;
    private DefaultTableModel invoicesTblModel;
    private DefaultTableModel invItemTblModel;
    private JLabel noOfInvoicesLbl;
    private JLabel invTotalLbl;

    private JTextField invDateTxtField;
    private JTextField customerNameTxtField;
    private boolean isNew;
    private String mainPath;
    private String detailsPath;

    public MainFrame() {
        super("Invoice Creator");

        // set frame properties
        setSize(1050, 600);
        setLayout(new GridLayout());
        setLocationRelativeTo(null);//set at center of screen
        setResizable(false);//prevent sizing to keep layout intact

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//close program on frame closing

        addPanels();
        addMenu();
    }

    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();

        //add file option to menu bar
        JMenu menu = new JMenu("File");

        //add load option
        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(new MyOpenListener(this, invoicesTblModel));//add actions

        //add save option
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new MySaveFileListener(this));//add actions
        saveItem.addActionListener(new MySaveListener(this));//add save button actions to save invoice items too

        //add options to frame
        menu.add(loadItem);
        menu.add(saveItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);


    }

    private void addPanels() {
        //create 2 panels to divide frame
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        //add every item to its side of the frame by adding to respective panel
        addLabels(leftPanel);
        addInvTable(leftPanel);
        addLeftButtons(leftPanel);
        addTxtFields(rightPanel);
        addInvItemTable(rightPanel);
        addRightButtons(rightPanel);
        add(leftPanel);
        add(rightPanel);
    }

    private void addInvTable(JPanel panel) {
        String[] columnNames = {"No.", "Date", "Customer", "Total"};


        int numRows = 3;
        invoicesTblModel = new DefaultTableModel(numRows, columnNames.length);//create model table
        invoicesTblModel.setColumnIdentifiers(columnNames);//add column names
        invoicesTbl = new JTable(invoicesTblModel);//create table from model
        panel.add(invoicesTbl);
        JScrollPane tblScrollPane = new JScrollPane(invoicesTbl);//add scroll pane to table
        tblScrollPane.setPreferredSize(new Dimension(470, 455));
        panel.add(tblScrollPane);//add scroll paned table to frame

    }

    private void addInvItemTable(JPanel panel) {
        String[] columnNames = {"No.", "Item Name", "Item Price", "Count", "Item Total"};

        JPanel invItemPanel = new JPanel(new FlowLayout());// create new panel for item table

        //set a compound border for table panel, so it contains a title
        invItemPanel.setBorder(
                new CompoundBorder(
                        BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Invoice Items"),
                        new EmptyBorder(5, 10, 10, 10)
                )
        );
        invItemPanel.setPreferredSize(new Dimension(500, 370));
        panel.add(invItemPanel);

        //create table using model
        int numRows = 3;
        invItemTblModel = new DefaultTableModel(numRows, columnNames.length);
        invItemTblModel.setColumnIdentifiers(columnNames);
        invItemTable = new JTable(invItemTblModel);

        //using a table listener so code happens on edit
        invItemTblModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getColumn() == 3 || e.getType() == 1/*when a whole row is added*/ || e.getColumn() == 2) {
                    //calculate item total when user enters the price and amount
                    int row = e.getLastRow();
                    String itemPrice = (String) invItemTblModel.getValueAt(row, 2);//get item price
                    String itemCount = (String) invItemTblModel.getValueAt(row, 3);//get item amount
                    try {//try and catch to use the condition on multiple occasions
                        //set the total on the last column
                        invItemTblModel.setValueAt(Integer.parseInt(itemPrice) * Integer.parseInt(itemCount), row, 4);
                    } catch (Exception ex) {
                    }

                } else if ((e.getLastRow() + 1) == invItemTblModel.getRowCount() && e.getType() == 0) {
                    //add new row when data is entered in the table's last row
                    invItemTblModel.addRow(new Object[]{null, null, null, null, null});

                } else if (e.getColumn() == 4 && Integer.parseInt((String) invItemTable.getValueAt(e.getLastRow(), 0)) == noOfInvoices) {
                    //calculate invoice total after an items total is calculated
                    int invTblTotal = (int) invItemTblModel.getValueAt(e.getLastRow(), 4);
                    invTotal += invTblTotal;
                    invTotalLbl.setText("  Invoice Total         " + invTotal);
                }
            }
        });
        invItemPanel.add(invItemTable);
        JScrollPane tblScrollPane = new JScrollPane(invItemTable);//add scrollpane to table
        tblScrollPane.setPreferredSize(new Dimension(470, 330));
        invItemPanel.add(tblScrollPane);//add to table panel created earlier in function

    }


    private void addLeftButtons(JPanel panel) {
        //create a panel for buttons to freely move them in frame
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout());
        btnPanel.setPreferredSize(new Dimension(450, 70));
        panel.add(btnPanel);//add btn panel to parent panel

        //create "create invoice" button and action
        JButton createInvBtn = new JButton("Create Invoice");
        createInvBtn.addActionListener(new MyCreateListener(this));

        //create "delete invoice" button and action
        JButton deleteInvBtn = new JButton("Delete Invoice");
        deleteInvBtn.addActionListener(new MyDeleteListener(this));

        //add to button panel
        btnPanel.add(createInvBtn);
        btnPanel.add(deleteInvBtn);

    }

    private void addRightButtons(JPanel panel) {
        //create a panel for buttons to freely move them in frame
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout());
        btnPanel.setPreferredSize(new Dimension(450, 70));
        panel.add(btnPanel);

        //create save button and add action
        JButton saveInvButton = new JButton("Save");
        saveInvButton.addActionListener(new MySaveListener(this));

        //create cancel button
        JButton cancelInvBtn = new JButton("Cancel");
        cancelInvBtn.addActionListener(new MyCancelListener(this));

        //add buttons to button panel
        btnPanel.add(saveInvButton);
        btnPanel.add(cancelInvBtn);

    }

    private void addLabels(JPanel panel) {
        //add label to panel
        JLabel tblLabel = new JLabel("Invoices Table");
        panel.add(tblLabel);

        //set the label to start from left
        tblLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void addTxtFields(JPanel panel) {
        //add new panel so label takes a whole line
        JPanel invNumberPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        invNumberPnl.setPreferredSize(new Dimension(450, 30));//set size to take a whole line of the parent panel
        panel.add(invNumberPnl);

        //create label and add it t label panel
        noOfInvoicesLbl = new JLabel("Invoice Number         " + noOfInvoices);
        invNumberPnl.add(noOfInvoicesLbl);

        //add a title for text field
        panel.add(new JLabel("  Invoice Date          "));
        invDateTxtField = new JTextField(30);// widen text field to fill panel
        invDateTxtField.setToolTipText("dd-mm-yy");//set tool tip to show required date format
        invDateTxtField.setEnabled(false);//disable text field until necessary
        panel.add(invDateTxtField);

        //add title for text field
        panel.add(new JLabel("  Customer Name    "));
        customerNameTxtField = new JTextField(30);//widen text field to fill panel
        customerNameTxtField.setEnabled(false);// set text field disabled untill necessary
        panel.add(customerNameTxtField);

        //create label that contains the total price of the invoice
        invTotalLbl = new JLabel("  Invoice Total         " + invTotal);
        panel.add(invTotalLbl);

    }


    //getters & setters
    public void setNoOfInvoices(int noOfInvoices) {
        this.noOfInvoices = noOfInvoices;
    }

    public JTable getInvoicesTbl() {
        return invoicesTbl;
    }

    public JTable getInvItemTable() {
        return invItemTable;
    }

    public String getMainPath() {
        return mainPath;
    }

    public void setMainPath(String mainPath) {
        this.mainPath = mainPath;
    }

    public String getDetailsPath() {
        return detailsPath;
    }

    public void setDetailsPath(String detailsPath) {
        this.detailsPath = detailsPath;
    }

    public int getNoOfInvoices() {
        return noOfInvoices;
    }

    public int getInvTotal() {
        return invTotal;
    }

    public DefaultTableModel getInvoicesTblModel() {
        return invoicesTblModel;
    }

    public DefaultTableModel getInvItemTblModel() {
        return invItemTblModel;
    }

    public JLabel getNoOfInvoicesLbl() {
        return noOfInvoicesLbl;
    }

    public JLabel getInvTotalLbl() {
        return invTotalLbl;
    }

    public JTextField getInvDateTxtField() {
        return invDateTxtField;
    }

    public JTextField getCustomerNameTxtField() {
        return customerNameTxtField;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setInvTotal(int invTotal) {
        this.invTotal = invTotal;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public int incrementNoOfInvoices() {
        return ++noOfInvoices;
    }
}
