package controllers;

import dao.StudentDAO;
import dao.BookDAO;
import models.Student;
import models.Book;
import services.Library;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.SceneManager;
import java.util.List;

public class BorrowController {

    @FXML private ComboBox<Student> studentComboBox;
    @FXML private ComboBox<Book> bookComboBox;
    @FXML private Button borrowButton;
    @FXML private Button backButton;
    @FXML private Button dashboardButton;
    @FXML private Label statusLabel;

    private StudentDAO studentDAO = new StudentDAO();
    private BookDAO bookDAO = new BookDAO();
    private Library library = new Library();

    @FXML
    public void initialize() {
        loadStudents();
        loadBooks();

        borrowButton.setOnAction(e -> handleBorrow());
        backButton.setOnAction(e -> SceneManager.goBack());
        dashboardButton.setOnAction(e -> SceneManager.goToDashboard());
    }

    private void loadStudents() {
        List<Student> students = studentDAO.getAllStudents();
        ObservableList<Student> studentList = FXCollections.observableArrayList(students);
        studentComboBox.setItems(studentList);

        // Show the student's name instead of the default toString()
        studentComboBox.setConverter(new javafx.util.StringConverter<Student>() {
            @Override
            public String toString(Student student) {
                return student == null ? "" : student.getName() + " (ID: " + student.getStudentID() + ")";
            }
            @Override
            public Student fromString(String string) {
                return null; // not needed, selection only
            }
        });
    }

    private void loadBooks() {
        List<Book> books = bookDAO.getAllBooks();
        // Only show books that are actually available to borrow
        List<Book> availableBooks = books.stream().filter(Book::isAvailable).toList();
        ObservableList<Book> bookList = FXCollections.observableArrayList(availableBooks);
        bookComboBox.setItems(bookList);

        bookComboBox.setConverter(new javafx.util.StringConverter<Book>() {
            @Override
            public String toString(Book book) {
                return book == null ? "" : book.getTitle() + " by " + book.getAuthor();
            }
            @Override
            public Book fromString(String string) {
                return null;
            }
        });
    }

    private void handleBorrow() {
        Student selectedStudent = studentComboBox.getValue();
        Book selectedBook = bookComboBox.getValue();

        if (selectedStudent == null || selectedBook == null) {
            statusLabel.setText("Please select both a student and a book.");
            return;
        }

        library.borrowBook(selectedStudent.getStudentID(), selectedBook.getBookID());
        statusLabel.setText(selectedStudent.getName() + " borrowed \"" + selectedBook.getTitle() + "\" successfully.");

        loadBooks(); // refresh so the borrowed book disappears from the available list
        bookComboBox.setValue(null);
    }
}