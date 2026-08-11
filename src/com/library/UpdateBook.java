package com.library;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateBook {

    public void showUpdateMenu() {

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("\n========== UPDATE BOOK ==========");

            System.out.println("1. Update Title");
            System.out.println("2. Update Author");
            System.out.println("3. Update Category");
            System.out.println("4. Update Quantity");
            System.out.println("5. Back");

            System.out.print("\nEnter your choice: ");

            String input = scanner.nextLine();

            int choice;

            try {
                choice = Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid choice! Please enter a number from 1 to 5."
                );

                continue;
            }

            switch (choice) {

            case 1:

                System.out.print("Enter Book ID: ");
                int bookId1 = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Enter New Title: ");
                String title = scanner.nextLine();

                String sql =
                        "UPDATE books SET title = ? WHERE book_id = ?";

                try (Connection connection = DBConnection.getConnection();
                     PreparedStatement statement =
                             connection.prepareStatement(sql)) {

                    statement.setString(1, title);
                    statement.setInt(2, bookId1);

                    int rowsUpdated = statement.executeUpdate();

                    if (rowsUpdated > 0) {

                        System.out.println(
                                "Book title updated successfully!"
                        );

                    } else {

                        System.out.println(
                                "Book not found!"
                        );
                    }

                } catch (SQLException e) {

                    System.out.println(
                            "Failed to update book."
                    );

                    e.printStackTrace();
                }

                break;

            case 2:

                System.out.print("Enter Book ID: ");
                int bookId2 = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Enter New Author: ");
                String author = scanner.nextLine();

                String authorSql =
                        "UPDATE books SET author = ? WHERE book_id = ?";

                try (Connection connection = DBConnection.getConnection();
                     PreparedStatement statement =
                             connection.prepareStatement(authorSql)) {

                    statement.setString(1, author);
                    statement.setInt(2, bookId2);

                    int rowsUpdated = statement.executeUpdate();

                    if (rowsUpdated > 0) {

                        System.out.println(
                                "Book author updated successfully!"
                        );

                    } else {

                        System.out.println(
                                "Book not found!"
                        );
                    }

                } catch (SQLException e) {

                    System.out.println(
                            "Failed to update author."
                    );

                    e.printStackTrace();
                }

                break;


            case 3:

                System.out.print("Enter Book ID: ");
                int bookId3 = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Enter New Category: ");
                String category = scanner.nextLine();

                String categorySql =
                        "UPDATE books SET category = ? WHERE book_id = ?";

                try (Connection connection = DBConnection.getConnection();
                     PreparedStatement statement =
                             connection.prepareStatement(categorySql)) {

                    statement.setString(1, category);
                    statement.setInt(2, bookId3);

                    int rowsUpdated = statement.executeUpdate();

                    if (rowsUpdated > 0) {

                        System.out.println(
                                "Book category updated successfully!"
                        );

                    } else {

                        System.out.println(
                                "Book not found!"
                        );
                    }

                } catch (SQLException e) {

                    System.out.println(
                            "Failed to update category."
                    );

                    e.printStackTrace();
                }

                break;

            case 4:

                System.out.print("Enter Book ID: ");
                int bookId4 = scanner.nextInt();

                System.out.print("Enter New Quantity: ");
                int quantity = scanner.nextInt();

                scanner.nextLine();

                if (quantity < 0) {

                    System.out.println(
                            "Quantity cannot be negative!"
                    );

                    break;
                }

                String quantitySql =
                        "UPDATE books SET quantity = ? WHERE book_id = ?";

                try (Connection connection = DBConnection.getConnection();
                     PreparedStatement statement =
                             connection.prepareStatement(quantitySql)) {

                    statement.setInt(1, quantity);
                    statement.setInt(2, bookId4);

                    int rowsUpdated = statement.executeUpdate();

                    if (rowsUpdated > 0) {

                        System.out.println(
                                "Book quantity updated successfully!"
                        );

                    } else {

                        System.out.println(
                                "Book not found!"
                        );
                    }

                } catch (SQLException e) {

                    System.out.println(
                            "Failed to update quantity."
                    );

                    e.printStackTrace();
                }

                break;

                case 5:

                    return;


                default:

                    System.out.println(
                            "Invalid choice! Please try again."
                    );
            }
        }
    }
}