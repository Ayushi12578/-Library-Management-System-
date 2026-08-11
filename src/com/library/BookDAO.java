package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDAO {

    // ADD BOOK
    public void addBook(Book book) {

        String sql = "INSERT INTO books (title, author, category, quantity) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getCategory());
            statement.setInt(4, book.getQuantity());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Book added successfully!");
            }

        } catch (SQLException e) {

            System.out.println("Failed to add book.");
            e.printStackTrace();
        }
    }


    // VIEW ALL BOOKS
    public void getAllBooks() {

        String sql = "SELECT * FROM books";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            boolean found = false;

            System.out.println("\n========== ALL BOOKS ==========");

            while (resultSet.next()) {

                found = true;

                System.out.println("-------------------------------");
                System.out.println("Book ID  : " + resultSet.getInt("book_id"));
                System.out.println("Title    : " + resultSet.getString("title"));
                System.out.println("Author   : " + resultSet.getString("author"));
                System.out.println("Category : " + resultSet.getString("category"));
                System.out.println("Quantity : " + resultSet.getInt("quantity"));
            }

            if (!found) {
                System.out.println("No books found.");
            }

            System.out.println("===============================");

        } catch (SQLException e) {

            System.out.println("Failed to fetch books.");
            e.printStackTrace();
        }
    }
}