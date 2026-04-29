package sms.dao;

import sms.db.DBConnection;
import sms.model.Student;

import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class StudentDAO {

    // Admin Login Validation
    public boolean validateLogin(String username, String password) {
        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM admin WHERE username=? AND password=?"
            );

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Add Student
    public void addStudent(Student s) throws Exception {

        Connection con = DBConnection.getConnection();

        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO students VALUES (?, ?, ?, ?)"
        );

        ps.setInt(1, s.getId());
        ps.setString(2, s.getName());
        ps.setString(3, s.getCourse());
        ps.setDouble(4, s.getMarks());

        ps.executeUpdate();

        ps.close();
        con.close();
    }

    // Update Student
    public void updateStudent(Student s) throws Exception {

        Connection con = DBConnection.getConnection();

        PreparedStatement ps = con.prepareStatement(
            "UPDATE students SET name=?, course=?, marks=? WHERE id=?"
        );

        ps.setString(1, s.getName());
        ps.setString(2, s.getCourse());
        ps.setDouble(3, s.getMarks());
        ps.setInt(4, s.getId());

        int rows = ps.executeUpdate();

        if (rows == 0) {
            throw new Exception("Student ID not found.");
        }

        ps.close();
        con.close();
    }

    // Delete Student
    public void deleteStudent(int id) throws Exception {

        Connection con = DBConnection.getConnection();

        PreparedStatement ps = con.prepareStatement(
            "DELETE FROM students WHERE id=?"
        );

        ps.setInt(1, id);

        int rows = ps.executeUpdate();

        if (rows == 0) {
            throw new Exception("Student ID not found.");
        }

        ps.close();
        con.close();
    }

    // View All Students
    public DefaultTableModel getAllStudents() throws Exception {

        String[] columns = {"ID", "Name", "Course", "Marks"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        Connection con = DBConnection.getConnection();

        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery("SELECT * FROM students");

        while (rs.next()) {

            Object[] row = {
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("course"),
                rs.getDouble("marks")
            };

            model.addRow(row);
        }

        rs.close();
        st.close();
        con.close();

        return model;
    }

    // Search Student By ID
    public Student searchStudent(int id) throws Exception {

        Connection con = DBConnection.getConnection();

        PreparedStatement ps = con.prepareStatement(
            "SELECT * FROM students WHERE id=?"
        );

        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        Student student = null;

        if (rs.next()) {
            student = new Student(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("course"),
                rs.getDouble("marks")
            );
        }

        rs.close();
        ps.close();
        con.close();

        return student;
    }
}