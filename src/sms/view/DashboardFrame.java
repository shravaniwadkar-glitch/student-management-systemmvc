package sms.view;

import sms.dao.StudentDAO;
import sms.model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private final Color SIDEBAR_COLOR = new Color(22, 34, 42);
    private final Color MAIN_BG = new Color(240, 242, 245);
    private final Color ACCENT_TEAL = new Color(58, 96, 115);

    private JTextField idField, nameField, courseField, marksField, searchField;
    private JTable studentTable;
    private StudentDAO dao;

    public DashboardFrame() {
        dao = new StudentDAO();
        setupFrame();
    }

    private void setupFrame() {

        setTitle("Advanced Student Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sidebar Panel
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(260, getHeight()));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setLayout(new BorderLayout());

        // Sidebar Top Section
        JPanel topSidebar = new JPanel();
        topSidebar.setBackground(SIDEBAR_COLOR);
        topSidebar.setLayout(new BoxLayout(topSidebar, BoxLayout.Y_AXIS));

        JLabel navTitle = new JLabel("SMS PANEL");
        navTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        navTitle.setForeground(Color.WHITE);
        navTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        topSidebar.add(Box.createVerticalStrut(40));
        topSidebar.add(navTitle);
        topSidebar.add(Box.createVerticalGlue());

        // Sidebar Bottom Section
        JPanel bottomSidebar = new JPanel();
        bottomSidebar.setBackground(SIDEBAR_COLOR);
        bottomSidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

        JButton logoutBtn = createSidebarButton("Logout / Exit");
        logoutBtn.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        bottomSidebar.add(logoutBtn);

        sidebar.add(topSidebar, BorderLayout.CENTER);
        sidebar.add(bottomSidebar, BorderLayout.SOUTH);

        // Main Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout(25, 25));
        contentPanel.setBackground(MAIN_BG);
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        contentPanel.add(createSearchPanel(), BorderLayout.NORTH);

        JPanel splitPanel = new JPanel(new GridBagLayout());
        splitPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Form Panel
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 0);
        splitPanel.add(createFormCard(), gbc);

        // Table Panel
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 25, 0, 0);
        splitPanel.add(createTableCard(), gbc);

        contentPanel.add(splitPanel, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        refreshTable();

        setVisible(true);
    }

    private JPanel createFormCard() {

        JPanel card = new JPanel();
        card.setLayout(new GridLayout(10, 1, 8, 8));
        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel label = new JLabel("Student Registration");
        label.setFont(new Font("SansSerif", Font.BOLD, 18));

        card.add(label);

        idField = createField("Student ID");
        nameField = createField("Full Name");
        courseField = createField("Course Name");
        marksField = createField("Final Marks");

        card.add(idField);
        card.add(nameField);
        card.add(courseField);
        card.add(marksField);
        card.add(Box.createVerticalStrut(10));

        JButton addBtn = createActionButton("Save Student", ACCENT_TEAL);
        JButton updateBtn = createActionButton("Update Record", new Color(100, 120, 140));
        JButton deleteBtn = createActionButton("Delete Student", new Color(217, 128, 128));

        addBtn.addActionListener(e -> handleAction("add"));
        updateBtn.addActionListener(e -> handleAction("update"));
        deleteBtn.addActionListener(e -> handleAction("delete"));

        card.add(addBtn);
        card.add(updateBtn);
        card.add(deleteBtn);

        return card;
    }

    private JPanel createTableCard() {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        studentTable = new JTable();
        studentTable.setRowHeight(35);
        studentTable.setFont(new Font("SansSerif", Font.PLAIN, 13));

        studentTable.getTableHeader().setBackground(new Color(245, 245, 245));
        studentTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private JPanel createSearchPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);

        JLabel searchLabel = new JLabel("Search ID:");
        searchLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 35));

        JButton searchBtn = createActionButton("Search", ACCENT_TEAL);

        searchBtn.addActionListener(e -> {
            try {

                if (searchField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Enter Student ID to search.");
                    return;
                }

                int id = Integer.parseInt(searchField.getText());

                Student s = dao.searchStudent(id);

                if (s != null) {
                    idField.setText(String.valueOf(s.getId()));
                    nameField.setText(s.getName());
                    courseField.setText(s.getCourse());
                    marksField.setText(String.valueOf(s.getMarks()));
                } else {
                    JOptionPane.showMessageDialog(this, "Student not found.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Search failed.");
            }
        });

        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchBtn);

        return panel;
    }

    private JTextField createField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
        return field;
    }

    private JButton createActionButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        return button;
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(220, 45));
        button.setMaximumSize(new Dimension(220, 45));
        button.setBackground(new Color(44, 62, 80));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void handleAction(String type) {

        try {

            if (type.equals("add")) {

                validateFields();

                dao.addStudent(
                    new Student(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        courseField.getText(),
                        Double.parseDouble(marksField.getText())
                    )
                );

                JOptionPane.showMessageDialog(this, "Student added successfully.");

            } else if (type.equals("update")) {

                validateFields();

                dao.updateStudent(
                    new Student(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        courseField.getText(),
                        Double.parseDouble(marksField.getText())
                    )
                );

                JOptionPane.showMessageDialog(this, "Student updated successfully.");

            } else if (type.equals("delete")) {

                if (idField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Enter Student ID.");
                    return;
                }

                dao.deleteStudent(Integer.parseInt(idField.getText()));

                JOptionPane.showMessageDialog(this, "Student deleted successfully.");
            }

            refreshTable();
            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid numeric input.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void validateFields() throws Exception {

        if (idField.getText().isEmpty() ||
            nameField.getText().isEmpty() ||
            courseField.getText().isEmpty() ||
            marksField.getText().isEmpty()) {

            throw new Exception("All fields are required.");
        }
    }

    private void refreshTable() {
        try {
            studentTable.setModel(dao.getAllStudents());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load student data.");
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        courseField.setText("");
        marksField.setText("");
        searchField.setText("");
    }
}