
import java.awt.EventQueue;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuGUI extends JFrame {

    JFrame frame;
    private JTable table;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField categoryField;

    private RestaurantDB db;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MenuGUI window = new MenuGUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MenuGUI() {
        db = new RestaurantDB();
        initialize();
        showMenuItems();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 550, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 11, 514, 187);
        frame.getContentPane().add(scrollPane);

        table = new JTable();
        table.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Item Name", "Price", "Category"}
        ));
        scrollPane.setViewportView(table);

        JLabel lblItemName = new JLabel("Item Name");
        lblItemName.setBounds(20, 220, 80, 14);
        frame.getContentPane().add(lblItemName);

        JLabel lblPrice = new JLabel("Price");
        lblPrice.setBounds(20, 245, 80, 14);
        frame.getContentPane().add(lblPrice);

        JLabel lblCategory = new JLabel("Category");
        lblCategory.setBounds(20, 270, 80, 14);
        frame.getContentPane().add(lblCategory);

        nameField = new JTextField();
        nameField.setBounds(110, 217, 150, 20);
        frame.getContentPane().add(nameField);
        nameField.setColumns(10);

        priceField = new JTextField();
        priceField.setBounds(110, 242, 150, 20);
        frame.getContentPane().add(priceField);
        priceField.setColumns(10);

        categoryField = new JTextField();
        categoryField.setBounds(110, 267, 150, 20);
        frame.getContentPane().add(categoryField);
        categoryField.setColumns(10);

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                String category = categoryField.getText();
                db.addMenuItem(name, price, category);
                showMenuItems();
                nameField.setText("");
                priceField.setText("");
                categoryField.setText("");
            }
        });
        btnAdd.setBounds(287, 216, 89, 23);
        frame.getContentPane().add(btnAdd);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//            	
            	String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                String category = categoryField.getText();
                
                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a menu item to update");
                } else {
                    String name2 = (String) table.getValueAt(row, 0);
                    db.updateMenuItem(name, price, category,name2);
                    showMenuItems();
                    nameField.setText("");
                    priceField.setText("");
                    categoryField.setText("");
                }
                
            }
});
btnUpdate.setBounds(287, 241, 89, 23);
frame.getContentPane().add(btnUpdate);

JButton btnDelete = new JButton("Delete");
btnDelete.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e) {
    int row = table.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(null, "Please select a menu item to delete");
    } else {
        String name = (String) table.getValueAt(row, 0);
        db.deleteMenuItem(name);
        showMenuItems();
        nameField.setText("");
        priceField.setText("");
        categoryField.setText("");
    }
}
});
btnDelete.setBounds(287, 266, 89, 23);
frame.getContentPane().add(btnDelete);

    JButton btnBack = new JButton("Back");
    btnBack.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Update().frame.setVisible(true);
            frame.dispose();
        }
    });
    btnBack.setBounds(386, 266, 89, 23);
    frame.getContentPane().add(btnBack);
   }

private void showMenuItems() {
DefaultTableModel model = (DefaultTableModel) table.getModel();
model.setRowCount(0);
try {
ResultSet rs = db.getMenuItems();
while (rs.next()) {
    String name = rs.getString("item_name");
    double price = rs.getDouble("price");
    String category = rs.getString("category");
    Object[] row = {name, price, category};
    model.addRow(row);
}
} catch (SQLException e) {
e.printStackTrace();
}
}

private class RestaurantDB {

private Connection conn;

public RestaurantDB() {
try {
    Class.forName("com.mysql.jdbc.Driver");
    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tree", "root", "Ammarfeb24");
} catch (ClassNotFoundException | SQLException e) {
    e.printStackTrace();
}
}

public ResultSet getMenuItems() throws SQLException {
Statement stmt = conn.createStatement();
return stmt.executeQuery("SELECT * FROM menu");
}

public void addMenuItem(String name, double price, String category) {
try {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO menu (item_name, price, category) VALUES (?, ?, ?)");
    pstmt.setString(1, name);
    pstmt.setDouble(2, price);
    pstmt.setString(3, category);
    pstmt.executeUpdate();
} catch (SQLException e) {
    e.printStackTrace();
}
}

public void updateMenuItem(String name, double price, String category, String name2) {
try {
    PreparedStatement pstmt = conn.prepareStatement("UPDATE menu SET item_name = ?, price = ?, category = ? WHERE item_name = ?");
    pstmt.setString(1, name);
    pstmt.setDouble(2, price);
    pstmt.setString(3, category);
    pstmt.setString(4, name2);
    pstmt.executeUpdate();
} catch (SQLException e) {
    e.printStackTrace();
}
}

public void deleteMenuItem(String name) {
try {
    PreparedStatement pstmt = conn.prepareStatement("DELETE FROM menu WHERE item_name = ?");
    pstmt.setString(1, name);
    pstmt.executeUpdate();
} catch (SQLException e) {
    e.printStackTrace();
}
}

}
}