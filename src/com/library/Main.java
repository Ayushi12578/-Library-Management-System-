package com.library;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        BookDAO bookDAO = new BookDAO();
        StudentDAO studentDAO = new StudentDAO();
        IssueDAO issueDAO = new IssueDAO();
        SearchBook searchBook = new SearchBook();
        UpdateBook updateBook=new UpdateBook();
        DeleteBook deletebook=new DeleteBook();
        SearchStudent seachstudent=new SearchStudent();
        IssuedBook issuedBook=new IssuedBook();
        OverdueBook overdueBook=new OverdueBook();
        Dashboard dashboard=new Dashboard();
        ReturnData returndata=new ReturnData();
       
        
        while (true) {

            System.out.println("\n=================================");
            System.out.println("    LIBRARY MANAGEMENT SYSTEM");
            System.out.println("=================================");

            System.out.println("1. Add Book");
            System.out.println("2. View All Books");
            System.out.println("3. Add Student");
            System.out.println("4. View All Students");
            System.out.println("5. Issue Book");
            System.out.println("6. Return Book");
            System.out.println("7. Search Book");
            System.out.println("8 Update Book");    
            System.out.println("9 DeleteBook");
            System.out.println("10 Search Stuent");
            System.out.println("11 Issued Book");
            System.out.println("12 Overdue Book");
            System.out.println("13 Dashboard");
            System.out.println("14 Return Data");
            System.out.println("15. Exit");
            

            System.out.print("\nEnter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                // ==========================================
                // 1. ADD BOOK
                // ==========================================

                case 1:

                    System.out.println(
                            "\n========== ADD BOOK =========="
                    );

                    System.out.print("Enter Book Title: ");
                    String title = scanner.nextLine();

                    System.out.print("Enter Author Name: ");
                    String author = scanner.nextLine();

                    System.out.print("Enter Category: ");
                    String category = scanner.nextLine();

                    System.out.print("Enter Quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();

                    Book book = new Book(
                            0,
                            title,
                            author,
                            category,
                            quantity
                    );

                    bookDAO.addBook(book);

                    break;


                // ==========================================
                // 2. VIEW ALL BOOKS
                // ==========================================

                case 2:

                    bookDAO.getAllBooks();

                    break;


                // ==========================================
                // 3. ADD STUDENT
                // ==========================================

                case 3:

                    System.out.println(
                            "\n========== ADD STUDENT =========="
                    );

                    System.out.print("Enter Student Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();

                    System.out.print("Enter Phone: ");
                    String phone = scanner.nextLine();

                    Student student = new Student(
                            0,
                            name,
                            email,
                            phone
                    );

                    studentDAO.addStudent(student);

                    break;


                // ==========================================
                // 4. VIEW ALL STUDENTS
                // ==========================================

                case 4:

                    studentDAO.getAllStudents();

                    break;


                // ==========================================
                // 5. ISSUE BOOK
                // ==========================================

                case 5:

                    System.out.println(
                            "\n========== ISSUE BOOK =========="
                    );

                    System.out.print("Enter Book ID: ");
                    int bookId = scanner.nextInt();

                    System.out.print("Enter Student ID: ");
                    int studentId = scanner.nextInt();

                    scanner.nextLine();

                    issueDAO.issueBook(
                            bookId,
                            studentId
                    );

                    break;


                // ==========================================
                // 6. RETURN BOOK
                // ==========================================

                case 6:

                    System.out.println(
                            "\n========================================"
                    );

                    System.out.println(
                            "             RETURN POLICY"
                    );

                    System.out.println(
                            "========================================"
                    );

                    System.out.println(
                            "• Book issue period : 7 days"
                    );

                    System.out.println(
                            "• Late return fine  : ₹10 per day"
                    );

                    System.out.println(
                            "• Due date          : Issue date + 7 days"
                    );

                    System.out.println(
                            "• Fine is calculated automatically"
                    );

                    System.out.println(
                            "========================================"
                    );

                    System.out.print(
                            "\nEnter Issue ID: "
                    );

                    int issueId = scanner.nextInt();
                    scanner.nextLine();

                    issueDAO.returnBook(issueId);

                    break;


                // ==========================================
                // 7. SEARCH BOOK
                // ==========================================

                case 7:

                    searchBook.showSearchMenu();

                    break;
                    
                case 8:
                	updateBook.showUpdateMenu();
                	break;
                case 9:
                	deletebook.deleteBookMenu();
                	break;
                case 10:

                	seachstudent.showSearchMenu();

                    break;
                case 11:
                	issuedBook.showIssuedBook();
                	break;
                case 12:
                	overdueBook.showOverdueBooks();
                	break;
                case 13:
                	dashboard.showDashboard();
                	break;
                case 14:
                	returndata.showReturnHistory();
                	break;
                // ==========================================
                // 15. EXIT
                // ==========================================

                case 15:

                    System.out.println(
                            "\nThank you for using "
                            + "Library Management System!"
                    );

                    scanner.close();

                    return;


                // ==========================================
                // INVALID CHOICE
                // ==========================================

                default:

                    System.out.println(
                            "\nInvalid choice! Please try again."
                    );
            }
        }
    }
}