package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class IssueDAO {

    // =========================================================
    // LIBRARY RETURN POLICY
    // =========================================================

    private static final int ALLOWED_DAYS = 7;
    private static final double PENALTY_PER_DAY = 10.0;

    // =========================================================
    // ISSUE BOOK
    // =========================================================

    public void issueBook(int bookId, int studentId) {

        String checkBookSql =
                "SELECT quantity FROM books WHERE book_id = ?";

        String checkStudentSql =
                "SELECT student_id FROM students WHERE student_id = ?";

        String issueSql =
                "INSERT INTO issue_record "
                + "(book_id, student_id, issue_date) "
                + "VALUES (?, ?, ?)";

        String updateQuantitySql =
                "UPDATE books "
                + "SET quantity = quantity - 1 "
                + "WHERE book_id = ?";

        try (Connection connection = DBConnection.getConnection()) {

            // =================================================
            // 1. CHECK BOOK
            // =================================================

            try (PreparedStatement statement =
                         connection.prepareStatement(checkBookSql)) {

                statement.setInt(1, bookId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (!resultSet.next()) {

                        System.out.println("Book not found!");
                        return;
                    }

                    int quantity =
                            resultSet.getInt("quantity");

                    if (quantity <= 0) {

                        System.out.println(
                                "Book is currently not available!"
                        );

                        return;
                    }
                }
            }

            // =================================================
            // 2. CHECK STUDENT
            // =================================================

            try (PreparedStatement statement =
                         connection.prepareStatement(checkStudentSql)) {

                statement.setInt(1, studentId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (!resultSet.next()) {

                        System.out.println(
                                "Student not found!"
                        );

                        return;
                    }
                }
            }

            // =================================================
            // 3. INSERT ISSUE RECORD
            // =================================================

            try (PreparedStatement statement =
                         connection.prepareStatement(issueSql)) {

                statement.setInt(1, bookId);
                statement.setInt(2, studentId);

                statement.setDate(
                        3,
                        java.sql.Date.valueOf(
                                LocalDate.now()
                        )
                );

                statement.executeUpdate();
            }

            // =================================================
            // 4. DECREASE BOOK QUANTITY
            // =================================================

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 updateQuantitySql)) {

                statement.setInt(1, bookId);

                statement.executeUpdate();
            }

            // =================================================
            // 5. SUCCESS MESSAGE
            // =================================================

            LocalDate issueDate = LocalDate.now();

            LocalDate dueDate =
                    issueDate.plusDays(ALLOWED_DAYS);

            System.out.println();
            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "          BOOK ISSUED SUCCESSFULLY"
            );

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "Issue Date   : " + issueDate
            );

            System.out.println(
                    "Due Date     : " + dueDate
            );

            System.out.println(
                    "Allowed Days : " + ALLOWED_DAYS
            );

            System.out.println(
                    "Late Fine    : ₹"
                    + PENALTY_PER_DAY
                    + " per day"
            );

            System.out.println(
                    "========================================"
            );

        } catch (SQLException e) {

            System.out.println(
                    "Failed to issue book."
            );

            e.printStackTrace();
        }
    }


    // =========================================================
    // RETURN BOOK
    // =========================================================

    public void returnBook(int issueId) {

        String selectSql =
                "SELECT book_id, issue_date, return_date "
                + "FROM issue_record "
                + "WHERE issue_id = ?";

        // IMPORTANT:
        // Database column is "penality", not "penalty".
        // Also there is a space before WHERE.

        String updateIssueSql =
                "UPDATE issue_record "
                + "SET return_date = ?, penality = ? "
                + "WHERE issue_id = ?";

        String updateBookSql =
                "UPDATE books "
                + "SET quantity = quantity + 1 "
                + "WHERE book_id = ?";


        try (Connection connection =
                     DBConnection.getConnection()) {

            int bookId;
            LocalDate issueDate;


            // =================================================
            // 1. FIND ISSUE RECORD
            // =================================================

            try (PreparedStatement statement =
                         connection.prepareStatement(selectSql)) {

                statement.setInt(1, issueId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (!resultSet.next()) {

                        System.out.println(
                                "Issue record not found!"
                        );

                        return;
                    }

                    bookId =
                            resultSet.getInt("book_id");


                    java.sql.Date sqlIssueDate =
                            resultSet.getDate("issue_date");


                    if (sqlIssueDate == null) {

                        System.out.println(
                                "Issue date not found!"
                        );

                        return;
                    }


                    issueDate =
                            sqlIssueDate.toLocalDate();


                    // =================================================
                    // CHECK ALREADY RETURNED
                    // =================================================

                    if (resultSet.getDate("return_date")
                            != null) {

                        System.out.println(
                                "This book has already been returned!"
                        );

                        return;
                    }
                }
            }


            // =================================================
            // 2. CALCULATE DATES
            // =================================================

            LocalDate returnDate =
                    LocalDate.now();

            LocalDate dueDate =
                    issueDate.plusDays(ALLOWED_DAYS);


            // =================================================
            // 3. CALCULATE TOTAL DAYS
            // =================================================

            long totalDays =
                    ChronoUnit.DAYS.between(
                            issueDate,
                            returnDate
                    );


            // =================================================
            // 4. CALCULATE LATE DAYS
            // =================================================

            long lateDays =
                    Math.max(
                            0,
                            totalDays - ALLOWED_DAYS
                    );


            // =================================================
            // 5. CALCULATE PENALTY
            // =================================================

            double penality =
                    lateDays * PENALTY_PER_DAY;


            // =================================================
            // 6. UPDATE ISSUE RECORD
            // =================================================

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 updateIssueSql)) {

                statement.setDate(
                        1,
                        java.sql.Date.valueOf(
                                returnDate
                        )
                );

                statement.setDouble(
                        2,
                        penality
                );

                statement.setInt(
                        3,
                        issueId
                );

                statement.executeUpdate();
            }


            // =================================================
            // 7. INCREASE BOOK QUANTITY
            // =================================================

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 updateBookSql)) {

                statement.setInt(1, bookId);

                statement.executeUpdate();
            }


            // =================================================
            // 8. DISPLAY RETURN SUMMARY
            // =================================================

            System.out.println();

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "           BOOK RETURN SUMMARY"
            );

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "Issue Date   : " + issueDate
            );

            System.out.println(
                    "Due Date     : " + dueDate
            );

            System.out.println(
                    "Return Date  : " + returnDate
            );

            System.out.println(
                    "Allowed Days : " + ALLOWED_DAYS
            );

            System.out.println(
                    "Total Days   : " + totalDays
            );

            System.out.println(
                    "Late Days    : " + lateDays
            );

            System.out.println(
                    "Penalty      : ₹" + penality
            );

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "Book returned successfully!"
            );


        } catch (SQLException e) {

            System.out.println(
                    "Failed to return book."
            );

            e.printStackTrace();
        }
    }
}