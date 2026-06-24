import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Main class to run the Library Management System
 * Version 2.1 (with due dates + late fees + global borrowed books view)
 * Author: Shankz
 */

class Main {

    // Helper method to read a date in DD/MM/YYYY format
    public static LocalDate readDate(Scanner scanner) {
        System.out.print("Enter date (DD/MM/YYYY): ");
        String input = scanner.nextLine();
        return LocalDate.parse(input, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static void main(String[] args) {

        Library library = new Library();
        library.loadData();
        Scanner scanner = new Scanner(System.in);

        int choice = 0;

        while (choice != 10) {

            System.out.println("\nWelcome to the Library Management System!");
            System.out.println("1. Add a book");
            System.out.println("2. Add a student");
            System.out.println("3. Borrow a book");
            System.out.println("4. Return a book");
            System.out.println("5. Display all borrowed books");
            System.out.println("6. Display all students");
            System.out.println("7. Display all books");
            System.out.println("8. Save data");
            System.out.println("9. Search");
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
                    System.out.print("Enter student ID: ");
                    int sId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter book ID: ");
                    int bId = scanner.nextInt();
                    scanner.nextLine();

                    LocalDate borrowDate = readDate(scanner);

                    library.borrowBook(sId, bId, borrowDate);
                    break;

                case 4:
                    System.out.print("Enter student ID: ");
                    int sId2 = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter book ID: ");
                    int bId2 = scanner.nextInt();
                    scanner.nextLine();

                    LocalDate returnDate = readDate(scanner);

                    library.returnBook(sId2, bId2, returnDate);
                    break;

                case 5:
                    library.displayAllBorrowedBooks();
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
                    System.out.println("\nSearch by:");
                    System.out.println("1. Book Title");
                    System.out.println("2. Book Author");
                    System.out.println("3. Student Name");
                    System.out.println("4. Book ID");
                    System.out.print("Enter choice: ");

                    int searchChoice = scanner.nextInt();
                    scanner.nextLine();

                        switch (searchChoice) {
                            case 1:
                                System.out.print("Enter book title: ");
                                String titleSearch = scanner.nextLine();
                                library.searchBookByTitle(titleSearch);
                                break;

                            case 2:
                                System.out.print("Enter author name: ");
                                String authorSearch = scanner.nextLine();
                                library.searchBookByAuthor(authorSearch);
                                break;

                            case 3:
                                System.out.print("Enter student name: ");
                                String studentSearch = scanner.nextLine();
                                library.searchStudent(studentSearch);
                                break;

                            case 4:
                                System.out.print("Enter book ID: ");
                                int bookIdSearch = scanner.nextInt();
                                scanner.nextLine();
                                library.searchBookById(bookIdSearch);
                                break;

                            default:
                                System.out.println("Invalid search option.");
                        }
                        break;

                case 10:
                    System.out.println("...Goodbye!...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}
