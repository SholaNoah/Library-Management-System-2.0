import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;


/** This class represents the library and manages a collection of books 
 * it runs bot the book and student class.
 * It is the brain behind eveything.
 * Version 2
 * @author Shankz
*/

class Library {
// Attributes
private ArrayList<Book> books; // Collection of books in the library
private ArrayList<Student> students; // Collection of students registered in the library
private ArrayList<BorrowRecord> borrowRecords; // Collection of borrowing records in the library


// Constructor
public Library() {
    books = new ArrayList<>();
    students = new ArrayList<>();
    borrowRecords = new ArrayList<>();
}

// Methods
// Add a new book to the Library
public void addBook(Book book) {
    books.add(book);
    System.out.println("Book added: " + book.getTitle());
}

// Register a new student in the Library
public void addStudent(Student student) {
    students.add(student);
    System.out.println("Student registered: " + student.getName());
}
// Find a book by its Title
public Book findBookByTitle(String title) {
    for (Book book : books) {
        if (book.getTitle().equalsIgnoreCase(title)) {
            return book; // Book found
        }
    }
    return null; // Book not found
}

// Find a student by their name 
public Student findStudentByName(String name) {
    for (Student student : students) {
        if (student.getName().equalsIgnoreCase(name)) {
            return student; // Student found
        }
    }
    return null; // Student not found
}

    // Find a student by their ID
    public Student findStudentById(int id) {
        for (Student student : students) {
            if (student.getStudentID() == id) {
                return student; // Student found
            }
        }
        return null; // Student not found
    }

// Find a book by its ID
public Book findBookById(int id) {
    for (Book book : books) {
        if (book.getBookID() == id) {
            return book; // Book found
        }
    }
    return null; // Book not found
}

// Borrow a book
public boolean borrowBook(int studentId, int bookId, LocalDate borrowDate) {
    Student student = findStudentById(studentId);
    Book book = findBookById(bookId);

    if (student != null && book != null) {
        if (book.borrowBook()) {

            BorrowRecord record = new BorrowRecord(student, book, borrowDate);
            borrowRecords.add(record);

            student.getBorrowedBooks().add(book);

            System.out.println(student.getName() + " borrowed " + book.getTitle() +
                               " (Due: " + record.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")");
            return true;
        } else {
            System.out.println("Sorry, " + book.getTitle() + " is not available.");
            return false;
        }
    } else {
        System.out.println("Invalid student ID or book ID.");
        return false;
    }
}


// Return a book
public void returnBook(int studentId, int bookId, LocalDate returnDate) {
    Student student = findStudentById(studentId);
    Book book = findBookById(bookId);

    if (student != null && book != null) {

        BorrowRecord record = null;
        for (BorrowRecord r : borrowRecords) {
            if (r.getStudent() == student && r.getBook() == book && !r.isReturned()) {
                record = r;
                break;
            }
        }

        if (record == null) {
            System.out.println("No active borrow record found for this book.");
            return;
        }

        int fee = LateFeeCalculator.calculateLateFee(record.getDueDate(), returnDate);

        record.markReturned(returnDate, fee);

        student.getBorrowedBooks().remove(book);
        book.returnBook();

        System.out.println(student.getName() + " returned " + book.getTitle());
        if (fee > 0) {
            System.out.println("Late fee: £" + fee);
        }
        return;
    }

    System.out.println("Invalid student ID or book ID.");
}


// Display all books in the library
public void displayAllBooks() {
    System.out.println("=========📖Books📖=========");
    for (Book book : books) {
        book.displayDetails();
        System.out.println(); // Add a blank line between books
    }
}

// Display all students in the library
public void displayAllStudents() {
    System.out.println("=========👩‍🎓Students👨‍🎓=========");
    for (Student student : students) {
        student.displayDetails();
        System.out.println(); // Add a blank line between students
    }
}

// Display all borrow records in the library
public void displayAllBorrowedBooks() {
    System.out.println("\n===== All Borrowed Books =====");

    boolean found = false;

    for (BorrowRecord record : borrowRecords) {
        if (!record.isReturned()) {
            found = true;

            System.out.println("- " + record.getBook().getTitle()
                + " | Borrowed by: " + record.getStudent().getName()
                + " | Due: " + record.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
    }

    if (!found) {
        System.out.println("No books are currently borrowed.");
    }
}


// Save data to a file
public void saveData() {
    try {
        FileWriter writer = new FileWriter("library_data.txt");

        // Save books
        writer.write("BOOKS\n");
        for (Book book : books) {
            writer.write("id=" + book.getBookID() + "\n");
            writer.write("title=" + book.getTitle() + "\n");
            writer.write("author=" + book.getAuthor() + "\n");
            writer.write("available=" + book.isAvailable() + "\n\n");
        }

        // Save students
        writer.write("STUDENTS\n");
        for (Student student : students) {
            writer.write("id=" + student.getStudentID() + "\n");
            writer.write("name=" + student.getName() + "\n");

            // Save borrowed book IDs
            StringBuilder borrowed = new StringBuilder();
            for (Book b : student.getBorrowedBooks()) {
                borrowed.append(b.getBookID()).append(",");
            }

            // Remove trailing comma
            if (borrowed.length() > 0) {
                borrowed.setLength(borrowed.length() - 1);
            }

            writer.write("borrowed=" + borrowed + "\n\n");
        }

        // Save borrow records
        writer.write("BORROW_RECORDS\n");
        for (BorrowRecord r : borrowRecords) {
        writer.write("studentId=" + r.getStudent().getStudentID() + "\n");
        writer.write("bookId=" + r.getBook().getBookID() + "\n");
        writer.write("borrowDate=" + r.getBorrowDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n");
        writer.write("dueDate=" + r.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n");
        writer.write("returned=" + r.isReturned() + "\n");
        writer.write("returnDate=" + (r.getReturnDate() == null ? "" : r.getReturnDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) + "\n");
        writer.write("lateFee=" + r.getLateFee() + "\n\n");
        }


        writer.close();
        System.out.println("Data saved successfully.");
    } catch (Exception e) {
        System.out.println("Error saving data: " + e.getMessage());
    }
}


// Load data from a file
public void loadData() {
    try {
        File file = new File("library_data.txt");
        if (!file.exists()) {
            System.out.println("No saved data found.");
            return;
        }

        Scanner reader = new Scanner(file);
        books.clear();
        students.clear();

        String section = "";

        while (reader.hasNextLine()) {
            String line = reader.nextLine().trim();

            if (line.equals("BOOKS")) {
                section = "BOOKS";
                continue;
            }
            if (line.equals("STUDENTS")) {
                section = "STUDENTS";
                continue;
            }
            if (line.equals("BORROW_RECORDS")) {
                section = "BORROW_RECORDS";
                continue;
            }
            if (line.isEmpty()) continue;

            if (section.equals("BOOKS")) {
                if (line.startsWith("id=")) {
                    int id = Integer.parseInt(line.substring(3));
                    String title = reader.nextLine().substring(6);
                    String author = reader.nextLine().substring(7);
                    boolean available = Boolean.parseBoolean(reader.nextLine().substring(10));

                    Book book = new Book(id, title, author);
                    book.setAvailable(available);
                    books.add(book);
                }
            }

            if (section.equals("STUDENTS")) {
                if (line.startsWith("id=")) {
                    int id = Integer.parseInt(line.substring(3));
                    String name = reader.nextLine().substring(5);
                    String borrowedLine = reader.nextLine().substring(9);

                    Student student = new Student(id, name);
                    students.add(student);

                    if (!borrowedLine.isEmpty()) {
                        String[] ids = borrowedLine.split(",");
                        for (String bookIdStr : ids) {
                            int bookId = Integer.parseInt(bookIdStr);
                            Book borrowedBook = findBookById(bookId);
                            if (borrowedBook != null) {
                               student.getBorrowedBooks().add(borrowedBook);
                            }
                        }
                    }
                }
            }

            if (section.equals("BORROW_RECORDS")) {
                if (line.startsWith("studentId=")) {
                    int studentId = Integer.parseInt(line.substring(10));
                    int bookId = Integer.parseInt(reader.nextLine().substring(7));

                    LocalDate borrowDate = LocalDate.parse(reader.nextLine().substring(11), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    LocalDate dueDate = LocalDate.parse(reader.nextLine().substring(8), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                    boolean returned = Boolean.parseBoolean(reader.nextLine().substring(9));

                    String returnDateStr = reader.nextLine().substring(11);
                    LocalDate returnDate = returnDateStr.isEmpty() ? null : LocalDate.parse(returnDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                    int lateFee = Integer.parseInt(reader.nextLine().substring(8));

                    Student student = findStudentById(studentId);
                    Book book = findBookById(bookId);

                    BorrowRecord record = new BorrowRecord(student, book, borrowDate);
                    record.setDueDate(dueDate);


                    if (returned) {
                        record.markReturned(returnDate, lateFee);
                    }

                    borrowRecords.add(record);
                }
            }

        }
        reader.close();
        System.out.println("Data loaded successfully.");
    } catch (Exception e) {
        System.out.println("Error loading data: " + e.getMessage());
    }
}


}