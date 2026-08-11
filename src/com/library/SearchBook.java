package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SearchBook {

    public void showSearchMenu() {

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("\n========== SEARCH BOOK ==========");
            System.out.println("1. Search by Title");
            System.out.println("2. Search by Author");
            System.out.println("3. Search by Category");
            System.out.println("4. Back");

            System.out.print("Enter your choice: ");

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

                    System.out.print("Enter Book Title: ");
                    String title = scanner.nextLine();

                    searchBook("title", title);

                    break;

                case 2:

                    System.out.print("Enter Author Name: ");
                    String author = scanner.nextLine();

                    searchBook("author", author);

                    break;

                case 3:

                    System.out.print("Enter Category: ");
                    String category = scanner.nextLine();

                    searchBook("category", category);

                    break;

                case 4:

                    return;

                default:

                    System.out.println("Invalid choice!");
            }
        }
    }


    private void searchBook(String column, String value) {

        String sql =
                "SELECT book_id, title, author, category, quantity "
                + "FROM books "
                + "WHERE " + column + " LIKE ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, "%" + value + "%");

            ResultSet resultSet = statement.executeQuery();

            boolean found = false;

            System.out.println("\n========== SEARCH RESULTS ==========");

            while (resultSet.next()) {

                found = true;

                System.out.println("-----------------------------------");

                System.out.println(
                        "Book ID  : "
                        + resultSet.getInt("book_id")
                );

                System.out.println(
                        "Title    : "
                        + resultSet.getString("title")
                );

                System.out.println(
                        "Author   : "
                        + resultSet.getString("author")
                );

                System.out.println(
                        "Category : "
                        + resultSet.getString("category")
                );

                System.out.println(
                        "Quantity : "
                        + resultSet.getInt("quantity")
                );
            }

            if (!found) {

                System.out.println(
                        "No book found for: " + value
                );
            }

            System.out.println(
                    "==================================="
            );

        } catch (SQLException e) {

            System.out.println("Search failed!");
            e.printStackTrace();
        }
    }
}