package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDAO {

    // ADD STUDENT
    public void addStudent(Student student) {

        String sql = "INSERT INTO students (name, email, phone) "
                   + "VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getName());
            statement.setString(2, student.getEmail());
            statement.setString(3, student.getPhone());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Student added successfully!");
            }

        } catch (SQLException e) {

            System.out.println("Failed to add student.");
            e.printStackTrace();
        }
    }


    // VIEW ALL STUDENTS
    public void getAllStudents() {

        String sql = "SELECT * FROM students";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            boolean found = false;

            System.out.println("\n========== ALL STUDENTS ==========");

            while (resultSet.next()) {

                found = true;

                System.out.println("-------------------------------");
                System.out.println("Student ID : "
                        + resultSet.getInt("student_id"));

                System.out.println("Name       : "
                        + resultSet.getString("name"));

                System.out.println("Email      : "
                        + resultSet.getString("email"));

                System.out.println("Phone      : "
                        + resultSet.getString("phone"));
            }

            if (!found) {
                System.out.println("No students found.");
            }

            System.out.println("==================================");

        } catch (SQLException e) {

            System.out.println("Failed to fetch students.");
            e.printStackTrace();
        }
    }
}