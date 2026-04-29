package sms.view;

import sms.dao.StudentDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final Color BG_DARK = new Color(22, 34, 42);
    private final Color TEAL_ACCENT = new Color(58, 96, 115);

    public LoginFrame() {
        setTitle("System Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainLayer = new JPanel(new GridBagLayout());
        mainLayer.setBackground(BG_DARK);

        // Login Card
        JPanel loginCard = new JPanel();
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setPreferredSize(new Dimension(380, 480));
        loginCard.setBackground(Color.WHITE);
        loginCard.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Logo/Title
        JLabel title = new JLabel("ADMIN ACCESS");
        title.setFont(new Font("Inter", Font.BOLD, 24));
        title.setForeground(TEAL_ACCENT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fields
        JTextField userField = new JTextField();
        userField.setMaximumSize(new Dimension(300, 50));
        userField.setBorder(BorderFactory.createTitledBorder("Username"));

        JPasswordField passField = new JPasswordField();
        passField.setMaximumSize(new Dimension(300, 50));
        passField.setBorder(BorderFactory.createTitledBorder("Password"));

        // Buttons
        JButton loginBtn = new JButton("Login");
        loginBtn.setMaximumSize(new Dimension(300, 45));
        loginBtn.setBackground(TEAL_ACCENT);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginBtn.addActionListener(e -> {
            StudentDAO dao = new StudentDAO();
            if (dao.validateLogin(userField.getText(), new String(passField.getPassword()))) {
                new DashboardFrame();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Access Denied");
            }
        });

        // Assembly
        loginCard.add(title);
        loginCard.add(Box.createVerticalStrut(50));
        loginCard.add(userField);
        loginCard.add(Box.createVerticalStrut(15));
        loginCard.add(passField);
        loginCard.add(Box.createVerticalStrut(30));
        loginCard.add(loginBtn);

        mainLayer.add(loginCard);
        add(mainLayer);
        setVisible(true);
    }
}