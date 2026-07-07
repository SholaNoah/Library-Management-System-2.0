package controllers;

import dao.BookDAO;
import dao.StudentDAO;
import models.Book;
import models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.SceneManager;
import java.util.List;

public class SearchController {

    @FXML private ComboBox<String> searchTypeComboBox;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button backButton;
    @FXML private Button dashboardButton;
    @FXML private Label statusLabel;

    @FXML private TableView<Book> booksResultTable;
    @FXML private TableColumn<Book, Integer> bookIdColumn;
    @FXML private TableColumn<Book, String> bookTitleColumn;
    @FXML private TableColumn<Book, String> bookAuthorColumn;
    @FXML private TableColumn<Book, Boolean> bookAvailableColumn;

    @FXML private TableView<Student> studentsResultTable;
    @FXML private TableColumn<Student, Integer> studentIdColumn;
    @FXML private TableColumn<Student, String> studentNameColumn;

    private BookDAO bookDAO = new BookDAO();
    private StudentDAO studentDAO = new StudentDAO();

    private static final String BY_TITLE = "Book Title";
    private static final String BY_STUDENT = "Student Name";
    private static final String BY_ID = "Book ID";

    @FXML
    public void initialize() {
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        searchTypeComboBox.setItems(FXCollections.observableArrayList(BY_TITLE, BY_STUDENT, BY_ID));
        searchTypeComboBox.setValue(BY_TITLE);

        searchButton.setOnAction(e -> handleSearch());
        backButton.setOnAction(e -> SceneManager.goBack());
        dashboardButton.setOnAction(e -> SceneManager.goToDashboard());
    }

    private void handleSearch() {
        String term = searchField.getText();
        String mode = searchTypeComboBox.getValue();

        if (term == null || term.isBlank()) {
            statusLabel.setText("Please enter a search term.");
            return;
        }

        switch (mode) {
            case BY_TITLE -> searchByTitle(term);
            case BY_STUDENT -> searchByStudentName(term);
            case BY_ID -> searchByBookId(term);
        }
    }

    private void searchByTitle(String term) {
        List<Book> results = bookDAO.searchByTitle(term);
        showBookResults(results);
    }

    private void searchByBookId(String term) {
        try {
            int id = Integer.parseInt(term.trim());
            Book book = bookDAO.getBookById(id);
            showBookResults(book == null ? List.of() : List.of(book));
        } catch (NumberFormatException e) {
            statusLabel.setText("Book ID must be a number.");
            hideBothTables();
        }
    }

    private void searchByStudentName(String term) {
        List<Student> results = studentDAO.searchByName(term);
        showStudentResults(results);
    }

    private void showBookResults(List<Book> books) {
        ObservableList<Book> list = FXCollections.observableArrayList(books);
        booksResultTable.setItems(list);

        booksResultTable.setVisible(true);
        booksResultTable.setManaged(true);
        studentsResultTable.setVisible(false);
        studentsResultTable.setManaged(false);

        statusLabel.setText(books.isEmpty() ? "No books found." : books.size() + " book(s) found.");
    }

    private void showStudentResults(List<Student> students) {
        ObservableList<Student> list = FXCollections.observableArrayList(students);
        studentsResultTable.setItems(list);

        studentsResultTable.setVisible(true);
        studentsResultTable.setManaged(true);
        booksResultTable.setVisible(false);
        booksResultTable.setManaged(false);

        statusLabel.setText(students.isEmpty() ? "No students found." : students.size() + " student(s) found.");
    }

    private void hideBothTables() {
        booksResultTable.setVisible(false);
        booksResultTable.setManaged(false);
        studentsResultTable.setVisible(false);
        studentsResultTable.setManaged(false);
    }
}