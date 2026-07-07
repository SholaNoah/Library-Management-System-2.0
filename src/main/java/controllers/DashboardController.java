package controllers;

import dao.DashboardStatsDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import utils.SceneManager;

public class DashboardController {

    @FXML private Button booksButton;
    @FXML private Button studentsButton;
    @FXML private Button borrowButton;
    @FXML private Button returnButton;
    @FXML private Button searchButton;

    @FXML private Label totalBooksLabel;
    @FXML private Label totalStudentsLabel;
    @FXML private Label activeBorrowsLabel;
    @FXML private Label mostBorrowedBookLabel;
    @FXML private Label mostActiveStudentLabel;

    private DashboardStatsDAO statsDAO = new DashboardStatsDAO();

    @FXML
    public void initialize() {
        booksButton.setOnAction(e -> SceneManager.switchTo("/fxml/books.fxml"));
        studentsButton.setOnAction(e -> SceneManager.switchTo("/fxml/students.fxml"));
        borrowButton.setOnAction(e -> SceneManager.switchTo("/fxml/borrow.fxml"));
        returnButton.setOnAction(e -> SceneManager.switchTo("/fxml/returnBook.fxml"));
        searchButton.setOnAction(e -> SceneManager.switchTo("/fxml/search.fxml"));

        loadStats();
    }

    private void loadStats() {
        totalBooksLabel.setText(String.valueOf(statsDAO.getTotalBooks()));
        totalStudentsLabel.setText(String.valueOf(statsDAO.getTotalStudents()));
        activeBorrowsLabel.setText(String.valueOf(statsDAO.getActiveBorrowsCount()));
        mostBorrowedBookLabel.setText(statsDAO.getMostBorrowedBook());
        mostActiveStudentLabel.setText(statsDAO.getMostActiveStudent());
    }
}