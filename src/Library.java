import java.io.File;
import java.io.FileWriter;
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

// Constructor
public Library() {
    books = new ArrayList<>();
    students = new ArrayList<>();   
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
public boolean borrowBook(int studentId, int bookId) {
    Student student = findStudentById(studentId);
    Book book = findBookById(bookId);

    if (student != null && book != null) {
        if (book.borrowBook()) { // Attempt to borrow the book
            student.borrowBook(book); // Add the book to the student's borrowed list
            System.out.println(student.getName() + " has borrowed " + book.getTitle());
            return true; // Borrowing successful
        } else {
            System.out.println("Sorry, " + book.getTitle() + " is currently not available.");
            return false; // Book is not available
        }
    } else {
        System.out.println("Invalid student ID or book ID.");
        return false; // Invalid IDs
    }
}

// Return a book
public void returnBook(int studentId, int bookId) {
    Student student = findStudentById(studentId);
    Book book = findBookById(bookId);

    if (student != null && book != null) {
        student.returnBook(book); // Remove the book from the student's borrowed list
        book.returnBook(); // Mark the book as available
        System.out.println(student.getName() + " has returned " + book.getTitle());
    } else {
        System.out.println("Invalid student ID or book ID.");
    }
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

// Save data to a file
public void saveData() {
    try {
        FileWriter writer = new FileWriter("library_data.txt");

        writer.write("BOOKS\n");
        for (Book book : books) {
            writer.write("id=" + book.getBookID() + "\n");
            writer.write("title=" + book.getTitle() + "\n");
            writer.write("author=" + book.getAuthor() + "\n");
            writer.write("available=" + book.isAvailable() + "\n\n");
        }

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
        }

        reader.close();
        System.out.println("Data loaded successfully.");
    } catch (Exception e) {
        System.out.println("Error loading data: " + e.getMessage());
    }
}


}