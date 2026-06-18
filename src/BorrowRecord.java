import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** This is the next phase of my Library Management System
 * This class represents the borrowing records of students in the library but with a twist
 * It doesn't just show borrowed books, it shows the date said book was borrowed,
 * shows the date for the book to be returned and shows the fee attached for late returns
 * @author Shankz
 */

public class BorrowRecord {
    private Student student;
    private Book book;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private boolean returned;
    private LocalDate returnDate;
    private int lateFee;

    public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
}


    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public BorrowRecord(Student student, Book book, LocalDate borrowDate) {
        this.student = student;
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(14);
        this.returned = false;
        this.lateFee = 0;
    }

    public Student getStudent() { return student; }
    public Book getBook() { return book; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isReturned() { return returned; }
    public LocalDate getReturnDate() { return returnDate; }
    public int getLateFee() { return lateFee; }

    public void markReturned(LocalDate returnDate, int lateFee) {
        this.returned = true;
        this.returnDate = returnDate;
        this.lateFee = lateFee;
    }

    public String format(LocalDate date) {
        return date == null ? "" : date.format(FORMATTER);
    }
}
