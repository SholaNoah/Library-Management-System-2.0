import java.util.Scanner;
/** Main class to run the Library Management System
 * It will create an instance of the Library and provide a simple interface for users to interact with the system.
 * Version 2
 * @author Shankz
 */

class Main {
    public static void main (String [] args)
    {
        Library library = new Library(); // Create an instance of the Library
        library.loadData(); // Load data from file if available
        Scanner scanner = new Scanner(System.in); // Scanner for user input
        int choice = 0; // Variable to store user choice

        while (choice != 10) { // Loop until the user chooses to exit   
        // Simple interface for demonstration
        System.out.println("Welcome to the Library Management System!");
        System.out.println("1. Add a book");
        System.out.println("2. Add a student");
        System.out.println("3. Borrow a book");
        System.out.println("4. Return a book");
        System.out.println("5. Display borrowed books");
        System.out.println("6. Display all students");
        System.out.println("7. Display all books");
        System.out.println("8. Save data");
        System.out.println("9. Load data");
        System.out.println("10. Exit");


        System.out.print("Enter your choice: ");
        choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter book ID: ");
                int bookId = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Enter book title: ");
                String title = scanner.nextLine();

                System.out.print("Enter author name: ");
                String author = scanner.nextLine();

                library.addBook(new Book(bookId, title, author));
                break;

            case 2:
                System.out.print("Enter student ID: ");
                int studentId = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Enter student name: ");
                String studentName = scanner.nextLine();

                library.addStudent(new Student(studentId, studentName));
                break;

            case 3:
                System.out.print("Enter student name: ");
                String StudentName = scanner.nextLine();
                Student student = library.findStudentByName(StudentName);
                if (student != null) {
                    System.out.print("Enter book title: ");
                    String bookTitle = scanner.nextLine();
                    Book book = library.findBookByTitle(bookTitle);
                    if (book != null) {
                        student.borrowBook(book);
                    } else {
                        System.out.println("Book not found.");
                    }
                } else {
                    System.out.println("Student not found.");
                }
                break;
            case 4:
                System.out.print("Enter student name: ");
                StudentName = scanner.nextLine();
                student = library.findStudentByName(StudentName);
                if (student != null) {
                    System.out.print("Enter book title: ");
                    String bookTitle = scanner.nextLine();
                    Book book = library.findBookByTitle(bookTitle);
                    if (book != null) {
                        student.returnBook(book);
                    } else {
                        System.out.println("Book not found.");
                    }
                } else {
                    System.out.println("Student not found.");
                }
                break;
            case 5:
                System.out.print("Enter student name: ");
                studentName = scanner.nextLine();
                student = library.findStudentByName(studentName);
                if (student != null) {
                    student.displayBorrowedBooks();
                } else {
                    System.out.println("Student not found.");
                }
                break;
            case 6:
                library.displayAllStudents();
                break;
            case 7:
                library.displayAllBooks();
                break;  

            case 8:
                library.saveData();
                break;

            case 9:
                library.loadData();
                break;

            case 10:
                System.out.println("...Goodbye!...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
            }

        }

        scanner.close(); // Close the scanner
    }
}

