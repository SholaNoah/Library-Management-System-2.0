package controllers;

import dao.BorrowDAO;
import dao.StudentDAO;
import dao.BookDAO;
import models.BorrowRecord;
import models.Student;
import models.Book;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.SceneManager;
import java.util.List;

public class ReturnController {

    @FXML private TableView<ActiveBorrowRow> borrowedTable;
    @FXML private TableColumn<ActiveBorrowRow, String> studentColumn;
    @FXML private TableColumn<ActiveBorrowRow, String> bookColumn;
    @FXML private TableColumn<ActiveBorrowRow, String> dueDateColumn;

    @FXML private Button returnButton;
    @FXML private Button refreshButton;
    @FXML private Button backButton;
    @FXML private Button dashboardButton;
    @FXML private Label statusLabel;

    private BorrowDAO borrowDAO = new BorrowDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private BookDAO bookDAO = new BookDAO();

    @FXML
    public void initialize() {
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        bookColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        returnButton.setOnAction(e -> handleReturn());
        refreshButton.setOnAction(e -> loadActiveBorrows());
        backButton.setOnAction(e -> SceneManager.goBack());
        dashboardButton.setOnAction(e -> SceneManager.goToDashboard());

        loadActiveBorrows();
    }

    private void loadActiveBorrows() {
        List<BorrowRecord> records = borrowDAO.getActiveBorrowRecords();
        ObservableList<ActiveBorrowRow> rows = FXCollections.observableArrayList();

        for (BorrowRecord record : records) {
            int studentId = record.getStudent().getStudentID();
            int bookId = record.getBook().getBookID();

            Student student = studentDAO.getStudentById(studentId);
            Book book = bookDAO.getBookById(bookId);
            Integer borrowId = borrowDAO.getBorrowId(studentId, bookId);

            if (student != null && book != null && borrowId != null) {
                rows.add(new ActiveBorrowRow(
                    borrowId,
                    student.getName(),
                    book.getTitle(),
                    record.format(record.getDueDate())
                ));
            }
        }

        borrowedTable.setItems(rows);
    }

    private void handleReturn() {
        ActiveBorrowRow selected = borrowedTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            statusLabel.setText("Please select a borrowed book to return.");
            return;
        }

        int lateFee = borrowDAO.returnBook(selected.getBorrowId());

        String message = "\"" + selected.getBookTitle() + "\" returned by " + selected.getStudentName() + ".";
        if (lateFee > 0) {
            message += " ⚠ Late fee due: £" + lateFee;
        }
        statusLabel.setText(message);

        loadActiveBorrows();
    }

    // Small helper class just to display enriched data in the table
    public static class ActiveBorrowRow {
        private final int borrowId;
        private final String studentName;
        private final String bookTitle;
        private final String dueDate;

        public ActiveBorrowRow(int borrowId, String studentName, String bookTitle, String dueDate) {
            this.borrowId = borrowId;
            this.studentName = studentName;
            this.bookTitle = bookTitle;
            this.dueDate = dueDate;
        }

        public int getBorrowId() { return borrowId; }
        public String getStudentName() { return studentName; }
        public String getBookTitle() { return bookTitle; }
        public String getDueDate() { return dueDate; }
    }
}