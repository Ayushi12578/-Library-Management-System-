package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReturnData {

    public void showReturnHistory() {

        String sql =
                "SELECT "
                + "ir.issue_id, "
                + "b.title, "
                + "s.name, "
                + "ir.issue_date, "
                + "ir.return_date, "
                + "ir.penality "
                + "FROM issue_record ir "
                + "JOIN books b ON ir.book_id = b.book_id "
                + "JOIN students s ON ir.student_id = s.student_id "
                + "WHERE ir.return_date IS NOT NULL "
                + "ORDER BY ir.return_date DESC";


        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql);

             ResultSet resultSet =
                     statement.executeQuery()) {


            boolean found = false;


            System.out.println();

            System.out.println(
                    "========================================================"
            );

            System.out.println(
                    "                  RETURN HISTORY"
            );

            System.out.println(
                    "========================================================"
            );


            while (resultSet.next()) {

                found = true;


                System.out.println(
                        "\nIssue ID    : "
                        + resultSet.getInt("issue_id")
                );

                System.out.println(
                        "Book        : "
                        + resultSet.getString("title")
                );

                System.out.println(
                        "Student     : "
                        + resultSet.getString("name")
                );

                System.out.println(
                        "Issue Date  : "
                        + resultSet.getDate("issue_date")
                );

                System.out.println(
                        "Return Date : "
                        + resultSet.getDate("return_date")
                );

                System.out.println(
                        "Penality    : ₹"
                        + resultSet.getDouble("penality")
                );

                System.out.println(
                        "--------------------------------------------------------"
                );
            }


            if (!found) {

                System.out.println(
                        "\nNo returned books found!"
                );
            }


            System.out.println(
                    "========================================================"
            );


        } catch (SQLException e) {

            System.out.println(
                    "Failed to load return history."
            );

            e.printStackTrace();
        }
    }
}