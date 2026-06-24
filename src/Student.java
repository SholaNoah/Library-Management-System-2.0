import java.util.ArrayList;

/** Student class in the library management system
 * It keeps track of the students who are registered in the library and their details.
 * This class will also manage the borrowing and returning of books by students.
 * Version 2
 * @author Shankz
 */

public class Student {
    // Attributes
    private int studentID;
    private String name;
    private ArrayList<Book> borrowedBooks; // List of books currently borrowed by the student


// Constructor
public Student(int studentID, String name) {
    this.studentID = studentID;
    this.name = name;
    this.borrowedBooks = new ArrayList<>(); // Initialize the list of borrowed books
}

// Methods
public int getStudentID() {
    return studentID;
}

public String getName() {
    return name;
}

public ArrayList<Book> getBorrowedBooks() {
    return borrowedBooks;
}

// Method to borrow a book
public void borrowBook(Book book) {
    if (book.borrowBook()) { // Attempt to borrow the book
        borrowedBooks.add(book); // Add the book to the student's list of borrowed books
        System.out.println(name + " has borrowed: " + book.getTitle());
    } else {
        System.out.println(name + " could not borrow: " + book.getTitle());
    }
}

// Method to return a book
public void returnBook(Book book) {
    if (borrowedBooks.remove(book)) { // Attempt to remove the book from the student's list of borrowed books
        book.setAvailable(true); // Mark the book as available in the library
        System.out.println(name + " has returned: " + book.getTitle());
    } else {
        System.out.println(name + " does not have: " + book.getTitle() + " to return.");
    }
}

// Method to display borrowed books
public void displayBorrowedBooks() {
    if (borrowedBooks.isEmpty()) {
        System.out.println(name + " has not borrowed any books.");
        return;
    }
    else {
    System.out.println(name + " has borrowed the following books:");
    for (Book book : borrowedBooks) {
        System.out.println("- " + book.getTitle() + " by " + book.getAuthor());
      }
   } 
}

// Method to display student details
public void displayDetails() {
    System.out.println("Student ID: " + studentID);
    System.out.println("Name: " + name);
}

}