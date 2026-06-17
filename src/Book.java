/** Version 2 of my Librarymanagement system
 * This class represents the books in the library
 * version 2
 * @author Shankz
 */

public class Book {
    // Attributes 
    private int bookID;
    private String title;
    private String author;
    private boolean isAvailable;

    // Constructor
    public Book(int bookID, String title, String author) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.isAvailable = true; // Books are available by default
    }
    // Methods
    public int getBookID() {
        return bookID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void displayDetails() {
        System.out.println("Book Id: "+bookID);
        System.out.println("Title: "+title);
        System.out.println("Author: "+author);
        System.out.println("Availabe: "+isAvailable);
    }

    // Algorithm for borrowing a book 
    public boolean borrowBook() {
        if (isAvailable) {
            isAvailable = false; // Mark the book as borrowed
            System.out.println("Book is currently available.");
            return true; // Borrowing successful
        } else {
            System.out.println("Book is not available for borrowing.");
            return false; // Book is not available
        }
    }

    // Algorithm for returning a book 
    public void returnBook() {
        isAvailable = true; // Mark the book as available
        System.out.println("Book returned successfully.");
    }
}