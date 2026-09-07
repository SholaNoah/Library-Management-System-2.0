package controllers;

import dao.DashboardStatsDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import utils.SceneManager;

public class DashboardController {

    @FXML private Button booksButton;
    @FXML private Button studentsButton;
    @FXML private Button borrowButton;
    @FXML private Button returnButton;
    @FXML private Button feesButton;
    @FXML private Button searchButton;

    @FXML private Label totalBooksLabel;
    @FXML private Label totalStudentsLabel;
    @FXML private Label activeBorrowsLabel;
    @FXML private Label overdueCountLabel;
    @FXML private Label mostBorrowedBookLabel;
    @FXML private Label mostActiveStudentLabel;

    @FXML private VBox overdueCard;
    @FXML private VBox activeStudentCard;

    private DashboardStatsDAO statsDAO = new DashboardStatsDAO();

    @FXML
    public void initialize() {
        booksButton.setOnAction(e -> SceneManager.switchTo("/fxml/books.fxml"));
        studentsButton.setOnAction(e -> SceneManager.switchTo("/fxml/students.fxml"));
        borrowButton.setOnAction(e -> SceneManager.switchTo("/fxml/borrow.fxml"));
        returnButton.setOnAction(e -> SceneManager.switchTo("/fxml/returnBook.fxml"));
        feesButton.setOnAction(e -> SceneManager.switchTo("/fxml/fees.fxml"));
        searchButton.setOnAction(e -> SceneManager.switchTo("/fxml/search.fxml"));

        overdueCard.setOnMouseClicked(e -> SceneManager.switchTo("/fxml/overdue.fxml"));
        activeStudentCard.setOnMouseClicked(e -> SceneManager.switchTo("/fxml/borrowHistory.fxml"));

        loadStats();
    }

    private void loadStats() {
        totalBooksLabel.setText(String.valueOf(statsDAO.getTotalBooks()));
        totalStudentsLabel.setText(String.valueOf(statsDAO.getTotalStudents()));
        activeBorrowsLabel.setText(String.valueOf(statsDAO.getActiveBorrowsCount()));
        mostBorrowedBookLabel.setText(statsDAO.getMostBorrowedBook());
        mostActiveStudentLabel.setText(statsDAO.getMostActiveStudent());

         int overdueCount = statsDAO.getOverdueCount();
        overdueCountLabel.setText(String.valueOf(overdueCount));

        if (overdueCount > 0) {
            overdueCountLabel.getStyleClass().remove("stat-value");
            if (!overdueCountLabel.getStyleClass().contains("stat-value-alert")) {
                overdueCountLabel.getStyleClass().add("stat-value-alert");
            }
        } else {
            overdueCountLabel.getStyleClass().remove("stat-value-alert");
            if (!overdueCountLabel.getStyleClass().contains("stat-value")) {
                overdueCountLabel.getStyleClass().add("stat-value");
            }
        }
    }

}