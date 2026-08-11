package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class OverdueBook {

    public void showOverdueBooks() {

        String sql =
                "SELECT "
                + "ir.issue_id, "
                + "b.title, "
                + "s.name, "
                + "ir.issue_date "
                + "FROM issue_record ir "
                + "JOIN books b ON ir.book_id = b.book_id "
                + "JOIN students s ON ir.student_id = s.student_id "
                + "WHERE ir.return_date IS NULL "
                + "ORDER BY ir.issue_date ASC";


        final int ALLOWED_DAYS = 7;
        final double PENALTY_PER_DAY = 10.0;


        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql);

             ResultSet resultSet =
                     statement.executeQuery()) {


            boolean found = false;

            LocalDate today = LocalDate.now();


            System.out.println();

            System.out.println(
                    "========================================================"
            );

            System.out.println(
                    "                    OVERDUE BOOKS"
            );

            System.out.println(
                    "========================================================"
            );


            while (resultSet.next()) {

                LocalDate issueDate =
                        resultSet.getDate("issue_date")
                                .toLocalDate();

                LocalDate dueDate =
                        issueDate.plusDays(ALLOWED_DAYS);


                // Check whether book is overdue

                if (today.isAfter(dueDate)) {

                    found = true;


                    long lateDays =
                            ChronoUnit.DAYS.between(
                                    dueDate,
                                    today
                            );


                    double penalty =
                            lateDays * PENALTY_PER_DAY;


                    System.out.println(
                            "\nIssue ID   : "
                            + resultSet.getInt("issue_id")
                    );

                    System.out.println(
                            "Book       : "
                            + resultSet.getString("title")
                    );

                    System.out.println(
                            "Student    : "
                            + resultSet.getString("name")
                    );

                    System.out.println(
                            "Issue Date : "
                            + issueDate
                    );

                    System.out.println(
                            "Due Date   : "
                            + dueDate
                    );

                    System.out.println(
                            "Late Days  : "
                            + lateDays
                    );

                    System.out.println(
                            "Fine       : ₹"
                            + penalty
                    );

                    System.out.println(
                            "--------------------------------------------------------"
                    );
                }
            }


            if (!found) {

                System.out.println(
                        "\nNo overdue books found!"
                );
            }


            System.out.println(
                    "========================================================"
            );


        } catch (SQLException e) {

            System.out.println(
                    "Failed to load overdue books."
            );

            e.printStackTrace();
        }
    }
}