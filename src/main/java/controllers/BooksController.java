package controllers;

import dao.BookDAO;
import models.Book;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;
import utils.SceneManager;
import java.util.Optional;

public class BooksController {

    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, Boolean> availableColumn;

    @FXML private Button addBookButton;
    @FXML private Button editBookButton;
    @FXML private Button deleteBookButton;
    @FXML private Button refreshButton;
    @FXML private Button backButton;
    @FXML private Button dashboardButton;

    private BookDAO bookDAO = new BookDAO();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

        addBookButton.setOnAction(e -> handleAddBook());
        editBookButton.setOnAction(e -> handleEditBook());
        deleteBookButton.setOnAction(e -> handleDeleteBook());
        refreshButton.setOnAction(e -> loadBooks());
        backButton.setOnAction(e -> SceneManager.goBack());
        dashboardButton.setOnAction(e -> SceneManager.goToDashboard());

        loadBooks();
    }

    private void loadBooks() {
        ObservableList<Book> books = FXCollections.observableArrayList(bookDAO.getAllBooks());
        booksTable.setItems(books);
    }

    private void handleAddBook() {
        Dialog<Book> dialog = buildBookDialog("Add Book", null);
        Optional<Book> result = dialog.showAndWait();
        result.ifPresent(book -> {
            if (!book.getTitle().isBlank() && !book.getAuthor().isBlank()) {
                bookDAO.addBook(book);
                loadBooks();
            }
        });
    }

    private void handleEditBook() {
        Book selected = booksTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No book selected", "Please select a book to edit.");
            return;
        }

        Dialog<Book> dialog = buildBookDialog("Edit Book", selected);
        Optional<Book> result = dialog.showAndWait();
        result.ifPresent(updatedBook -> {
            if (!updatedBook.getTitle().isBlank() && !updatedBook.getAuthor().isBlank()) {
                bookDAO.updateBook(updatedBook);
                loadBooks();
            }
        });
    }

    private void handleDeleteBook() {
        Book selected = booksTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No book selected", "Please select a book to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Book");
        confirm.setHeaderText("Delete \"" + selected.getTitle() + "\"?");
        confirm.setContentText("This cannot be undone.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            bookDAO.deleteBook(selected.getBookID());
            loadBooks();
        }
    }

    // Shared dialog builder for both Add and Edit — prefilled if editing an existing book
    private Dialog<Book> buildBookDialog(String title, Book existing) {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(existing == null ? "Enter the new book's details" : "Update the book's details");

        ButtonType confirmButtonType = new ButtonType(existing == null ? "Add" : "Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextField authorField = new TextField();
        authorField.setPromptText("Author");

        if (existing != null) {
            titleField.setText(existing.getTitle());
            authorField.setText(existing.getAuthor());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == confirmButtonType) {
                int id = existing == null ? 0 : existing.getBookID();
                return new Book(id, titleField.getText(), authorField.getText());
            }
            return null;
        });

        return dialog;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}