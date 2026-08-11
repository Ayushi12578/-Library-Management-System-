package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteBook {

    public void deleteBookMenu() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("\n========== DELETE BOOK ==========");

        System.out.print("Enter Book ID: ");

        String input = scanner.nextLine();

        int bookId;

        try {

            bookId = Integer.parseInt(input);

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid Book ID! Please enter a number."
            );

            return;
        }

        System.out.print(
                "Are you sure you want to delete this book? (Y/N): "
        );

        String confirmation = scanner.nextLine();

        if (!confirmation.equalsIgnoreCase("Y")) {

            System.out.println(
                    "Delete operation cancelled."
            );

            return;
        }

        deleteBook(bookId);
    }


    // =====================================================
    // DELETE BOOK FROM DATABASE
    // =====================================================

    private void deleteBook(int bookId) {

        String checkBookSql =
                "SELECT book_id FROM books WHERE book_id = ?";

        String checkIssueSql =
                "SELECT issue_id FROM issue_record "
                + "WHERE book_id = ? "
                + "AND return_date IS NULL";

        String deleteSql =
                "DELETE FROM books WHERE book_id = ?";


        try (Connection connection =
                     DBConnection.getConnection()) {


            // =================================================
            // 1. CHECK BOOK EXISTS
            // =================================================

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 checkBookSql)) {

                statement.setInt(1, bookId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (!resultSet.next()) {

                        System.out.println(
                                "Book not found!"
                        );

                        return;
                    }
                }
            }


            // =================================================
            // 2. CHECK ACTIVE ISSUE
            // =================================================

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 checkIssueSql)) {

                statement.setInt(1, bookId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (resultSet.next()) {

                        System.out.println();
                        System.out.println(
                                "Cannot delete this book!"
                        );

                        System.out.println(
                                "This book is currently issued "
                                + "to a student."
                        );

                        return;
                    }
                }
            }


            // =================================================
            // 3. DELETE BOOK
            // =================================================

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 deleteSql)) {

                statement.setInt(1, bookId);

                int rowsDeleted =
                        statement.executeUpdate();

                if (rowsDeleted > 0) {

                    System.out.println();
                    System.out.println(
                            "Book deleted successfully!"
                    );

                } else {

                    System.out.println(
                            "Book could not be deleted."
                    );
                }
            }


        } catch (SQLException e) {

            System.out.println(
                    "Failed to delete book."
            );

            e.printStackTrace();
        }
    }
}