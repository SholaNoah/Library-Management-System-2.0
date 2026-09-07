package controllers;

import dao.BorrowDAO;
import dao.BorrowDAO.BorrowHistoryRecord;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.SceneManager;

public class BorrowHistoryController {

    @FXML private TableView<BorrowHistoryRecord> historyTable;
    @FXML private TableColumn<BorrowHistoryRecord, String> studentColumn;
    @FXML private TableColumn<BorrowHistoryRecord, String> bookColumn;
    @FXML private TableColumn<BorrowHistoryRecord, String> borrowDateColumn;
    @FXML private TableColumn<BorrowHistoryRecord, String> returnDateColumn;
    @FXML private TableColumn<BorrowHistoryRecord, String> statusColumn;
    @FXML private TableColumn<BorrowHistoryRecord, Integer> feeColumn;

    @FXML private Button backButton;
    @FXML private Button dashboardButton;
    @FXML private Button refreshButton;

    private BorrowDAO borrowDAO = new BorrowDAO();

    @FXML
    public void initialize() {
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        bookColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        feeColumn.setCellValueFactory(new PropertyValueFactory<>("lateFee"));

        backButton.setOnAction(e -> SceneManager.goBack());
        dashboardButton.setOnAction(e -> SceneManager.goToDashboard());
        refreshButton.setOnAction(e -> loadHistory());

        loadHistory();
    }

    private void loadHistory() {
        ObservableList<BorrowHistoryRecord> rows = FXCollections.observableArrayList(borrowDAO.getBorrowHistory());
        historyTable.setItems(rows);
    }
}