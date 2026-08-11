package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SearchStudent {

    public void showSearchMenu() {

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("\n========== SEARCH STUDENT ==========");

            System.out.println("1. Search by Name");
            System.out.println("2. Search by Email");
            System.out.println("3. Search by Phone");
            System.out.println("4. Back");

            System.out.print("\nEnter your choice: ");

            String input = scanner.nextLine();

            int choice;

            try {

                choice = Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid choice! Please enter a number from 1 to 4."
                );

                continue;
            }

            switch (choice) {

                case 1:

                    System.out.print("Enter Student Name: ");

                    String name = scanner.nextLine();

                    searchByName(name);

                    break;


                case 2:

                    System.out.print("Enter Student Email: ");

                    String email = scanner.nextLine();

                    searchByEmail(email);

                    break;


                case 3:

                    System.out.print("Enter Student Phone: ");

                    String phone = scanner.nextLine();

                    searchByPhone(phone);

                    break;


                case 4:

                    return;


                default:

                    System.out.println(
                            "Invalid choice! Please try again."
                    );
            }
        }
    }


    // =====================================================
    // SEARCH BY NAME
    // =====================================================

    private void searchByName(String name) {

        String sql =
                "SELECT * FROM students "
                + "WHERE name LIKE ?";

        searchStudents(sql, "%" + name + "%");
    }


    // =====================================================
    // SEARCH BY EMAIL
    // =====================================================

    private void searchByEmail(String email) {

        String sql =
                "SELECT * FROM students "
                + "WHERE email LIKE ?";

        searchStudents(sql, "%" + email + "%");
    }


    // =====================================================
    // SEARCH BY PHONE
    // =====================================================

    private void searchByPhone(String phone) {

        String sql =
                "SELECT * FROM students "
                + "WHERE phone LIKE ?";

        searchStudents(sql, "%" + phone + "%");
    }


    // =====================================================
    // COMMON SEARCH METHOD
    // =====================================================

    private void searchStudents(
            String sql,
            String searchValue) {

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, searchValue);

            ResultSet resultSet =
                    statement.executeQuery();

            boolean found = false;

            System.out.println(
                    "\n========== SEARCH RESULTS =========="
            );


            while (resultSet.next()) {

                found = true;

                System.out.println(
                        "-----------------------------------"
                );

                System.out.println(
                        "Student ID : "
                        + resultSet.getInt("student_id")
                );

                System.out.println(
                        "Name       : "
                        + resultSet.getString("name")
                );

                System.out.println(
                        "Email      : "
                        + resultSet.getString("email")
                );

                System.out.println(
                        "Phone      : "
                        + resultSet.getString("phone")
                );
            }


            if (!found) {

                System.out.println(
                        "No student found!"
                );
            }


            System.out.println(
                    "==================================="
            );


        } catch (SQLException e) {

            System.out.println(
                    "Failed to search students."
            );

            e.printStackTrace();
        }
    }
}