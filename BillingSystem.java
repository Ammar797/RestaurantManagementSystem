import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;

public class BillingSystem extends JFrame {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/tree";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Ammarfeb24";

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private JPanel menuPanel;
    private JPanel cartPanel;
    private JLabel totalLabel;
    private JLabel mobileNumberLabel;
    private JTextField mobileNumberTextField;
    private JButton checkoutButton;

    private BigDecimal totalAmount;
    private String mobileNumber;

    private Connection conn;

    public BillingSystem() {
        super("Billing System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        totalAmount = BigDecimal.ZERO;
        // Set up database connection
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        // Set up GUI components
        menuPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        cartPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        totalLabel = new JLabel("Total: Rs0.00");
        mobileNumberLabel = new JLabel("Enter mobile number: ");
        mobileNumberTextField = new JTextField(10);
        checkoutButton = new JButton("Checkout");

        // Retrieve data from database and add menu items to GUI
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM menu");
            while (rs.next()) {
                String itemName = rs.getString("item_name");
                BigDecimal price = rs.getBigDecimal("price");
                String category = rs.getString("category");
                JButton addButton = new JButton("Add to Cart");
                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addToCart(itemName, price);
                    }
                });
                menuPanel.add(new JLabel(itemName));
                menuPanel.add(new JLabel("Rs" + price));
                menuPanel.add(addButton);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add components to main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(cartPanel, BorderLayout.CENTER);
        mainPanel.add(totalLabel, BorderLayout.SOUTH);

        JPanel checkoutPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        checkoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel nameLabel = new JLabel("Enter customer name: ");
        JTextField nameTextField = new JTextField(10);
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        namePanel.add(nameLabel);
        namePanel.add(nameTextField);
        checkoutPanel.add(namePanel);
        JPanel mobilePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        mobilePanel.add(mobileNumberLabel);
        mobilePanel.add(mobileNumberTextField);
        checkoutPanel.add(mobilePanel);
        checkoutPanel.add(checkoutButton);
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkout(nameTextField.getText());
            }
        });
        mainPanel.add(checkoutPanel, BorderLayout.NORTH);
        


        add(mainPanel);
        setVisible(true);
        
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Update().frame.setVisible(true);
//                frame.dispose();
            }
        });
        checkoutPanel.add(btnBack);
//        this.getContentPane().add(btnBack);

    }
    

    private void addToCart(String itemName, BigDecimal price) {
        // Create a new item panel and add to cart panel
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        itemPanel.add(new JLabel(itemName));

        // Add spinner for quantity selection
        SpinnerModel quantityModel = new SpinnerNumberModel(1, 1, 100, 1);
        JSpinner quantitySpinner = new JSpinner(quantityModel);
        itemPanel.add(quantitySpinner);
        // Add price label
        BigDecimal totalPrice = price.multiply(new BigDecimal((int) quantitySpinner.getValue()));
        JLabel priceLabel = new JLabel("Rs" + totalPrice);
        itemPanel.add(priceLabel);

        // Add remove button
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cartPanel.remove(itemPanel);
                cartPanel.revalidate();
                cartPanel.repaint();
                updateTotal();
            }
        });
        itemPanel.add(removeButton);

        // Add item panel to cart panel
        cartPanel.add(itemPanel);
        cartPanel.revalidate();
        cartPanel.repaint();

        // Update total amount
        totalAmount = totalAmount.add(totalPrice);
        updateTotal();
    }

    private void updateTotal() {
        totalLabel.setText("Total: Rs" + totalAmount);
    }

    
    private void checkout(String customerName) {
        // Get mobile number from text field
        mobileNumber = mobileNumberTextField.getText();

        // Save order details to database
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO orders (mobile_number, customer_name, total_amount) VALUES (?, ?, ?)");
            stmt.setString(1, mobileNumber);
            stmt.setString(2, customerName);
            stmt.setBigDecimal(3, totalAmount);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Display confirmation message
        JOptionPane.showMessageDialog(this, "Order placed successfully for " + customerName + "! Total amount: Rs" + totalAmount);
    }

    public static void main(String[] args) {
        new BillingSystem();
    }
}


