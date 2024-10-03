import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ViewBill extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private JPanel buttonPanel;
    private JButton deleteButton;
    private JButton updateButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet rs;

    public ViewBill() {
        // Set up the UI
        setTitle("View Bill");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the table and table model
        tableModel = new DefaultTableModel(new String[] { "Mobile Number", "Customer Name", "Total Amount" }, 0);
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create the button panel and buttons
        buttonPanel = new JPanel();
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Get the mobile number from the selected row
                    String mobileNumber = (String) table.getValueAt(selectedRow, 0);

                    try {
                        // Delete the row from the database
                        stmt = conn.prepareStatement("DELETE FROM orders WHERE mobile_number = ?");
                        stmt.setString(1, mobileNumber);
                        stmt.executeUpdate();

                        // Remove the row from the table model
                        tableModel.removeRow(selectedRow);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Get the data from the selected row
                    String mobileNumber = (String) table.getValueAt(selectedRow, 0);
                    String customerName = (String) table.getValueAt(selectedRow, 1);
                    double totalAmount = (double) table.getValueAt(selectedRow, 2);

                    try {
                        // Update the row in the database
                        stmt = conn.prepareStatement("UPDATE orders SET customer_name = ?, total_amount = ? WHERE mobile_number = ?");
                        stmt.setString(1, customerName);
                        stmt.setDouble(2, totalAmount);
                        stmt.setString(3, mobileNumber);
                        stmt.executeUpdate();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Update().frame.setVisible(true);
            }
        });
        buttonPanel.add(deleteButton);
        buttonPanel.add(btnBack);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Connect to the database and populate the table model
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tree", "root", "Ammarfeb24");

            stmt = conn.prepareStatement("SELECT * FROM orders");
            rs = stmt.executeQuery();

            while (rs.next())
            {
            	String mobileNumber = rs.getString("mobile_number");
            	String customerName = rs.getString("customer_name");
            	double totalAmount = rs.getDouble("total_amount");
                Object[] row = { mobileNumber, customerName, totalAmount };
                tableModel.addRow(row);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ViewBill viewBill = new ViewBill();
        viewBill.setVisible(true);
    }
}
