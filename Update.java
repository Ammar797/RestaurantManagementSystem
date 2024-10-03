import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JScrollBar;
import javax.swing.border.EtchedBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Update {

	JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Update window = new Update();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Update() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 664, 460);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("MENU");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				MenuGUI updateFrame = new MenuGUI();
	            updateFrame.frame.setVisible(true);
	            frame.dispose();
			}
		});
		btnNewButton.setBounds(123, 140, 156, 83);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnBilling = new JButton("BILLING");
		btnBilling.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        BillingSystem billingSystem = new BillingSystem();
		        billingSystem.setVisible(true);
		        frame.dispose();
		    }
		});

		btnBilling.setBounds(375, 140, 156, 83);
		frame.getContentPane().add(btnBilling);
		
		JButton btnViewBills = new JButton("VIEW BILLS");
		btnViewBills.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        ViewBill bill = new ViewBill();
		        bill.setVisible(true);
		        frame.dispose();
		    }
		});
		btnViewBills.setBounds(249, 268, 156, 83);
		frame.getContentPane().add(btnViewBills);
		
		JLabel lblNewLabel = new JLabel("Welcome To AOB Restaurant !!");
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 26));
		lblNewLabel.setForeground(Color.PINK);
		lblNewLabel.setBounds(135, 31, 394, 68);
		frame.getContentPane().add(lblNewLabel);
	}
}

