package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Dashboard {

    private static final int ALLOWED_DAYS = 7;
    private static final double PENALTY_PER_DAY = 10.0;

    public void showDashboard() {

        int totalBooks = 0;
        int availableBooks = 0;
        int issuedBooks = 0;
        int totalStudents = 0;
        int overdueBooks = 0;
        double pendingFine = 0.0;

        try (Connection connection = DBConnection.getConnection()) {

            // ==========================================
            // 1. TOTAL BOOKS
            // ==========================================

            String totalBooksSql =
                    "SELECT COUNT(*) FROM books";

            try (PreparedStatement statement =
                         connection.prepareStatement(totalBooksSql);
                 ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    totalBooks = resultSet.getInt(1);
                }
            }


            // ==========================================
            // 2. AVAILABLE BOOKS
            // ==========================================

            String availableBooksSql =
                    "SELECT COALESCE(SUM(quantity), 0) FROM books";

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 availableBooksSql);
                 ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    availableBooks = resultSet.getInt(1);
                }
            }


            // ==========================================
            // 3. CURRENTLY ISSUED BOOKS
            // ==========================================

            String issuedBooksSql =
                    "SELECT COUNT(*) "
                    + "FROM issue_record "
                    + "WHERE return_date IS NULL";

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 issuedBooksSql);
                 ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    issuedBooks = resultSet.getInt(1);
                }
            }


            // ==========================================
            // 4. TOTAL STUDENTS
            // ==========================================

            String totalStudentsSql =
                    "SELECT COUNT(*) FROM students";

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 totalStudentsSql);
                 ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    totalStudents = resultSet.getInt(1);
                }
            }


            // ==========================================
            // 5. OVERDUE BOOKS + PENDING FINE
            // ==========================================

            String overdueSql =
                    "SELECT issue_date "
                    + "FROM issue_record "
                    + "WHERE return_date IS NULL";

            try (PreparedStatement statement =
                         connection.prepareStatement(overdueSql);
                 ResultSet resultSet =
                         statement.executeQuery()) {

                LocalDate today = LocalDate.now();

                while (resultSet.next()) {

                    LocalDate issueDate =
                            resultSet.getDate("issue_date")
                                    .toLocalDate();

                    LocalDate dueDate =
                            issueDate.plusDays(ALLOWED_DAYS);

                    if (today.isAfter(dueDate)) {

                        overdueBooks++;

                        long lateDays =
                                ChronoUnit.DAYS.between(
                                        dueDate,
                                        today
                                );

                        pendingFine +=
                                lateDays * PENALTY_PER_DAY;
                    }
                }
            }


            // ==========================================
            // 6. DISPLAY DASHBOARD
            // ==========================================

            System.out.println();

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "           LIBRARY DASHBOARD"
            );

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "📚 Total Books       : " + totalBooks
            );

            System.out.println(
                    "📖 Available Books   : " + availableBooks
            );

            System.out.println(
                    "📕 Issued Books      : " + issuedBooks
            );

            System.out.println(
                    "👨‍🎓 Total Students    : " + totalStudents
            );

            System.out.println(
                    "⚠️ Overdue Books     : " + overdueBooks
            );

            System.out.println(
                    "💰 Pending Fine      : ₹" + pendingFine
            );

            System.out.println(
                    "========================================"
            );


        } catch (SQLException e) {

            System.out.println(
                    "Failed to load dashboard."
            );

            e.printStackTrace();
        }
    }
}