package controllers;

import dao.BorrowDAO;
import dao.BorrowDAO.FeeRecord;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.SceneManager;
import java.util.List;

public class FeesController {

    @FXML private TableView<FeeRecord> feesTable;
    @FXML private TableColumn<FeeRecord, String> studentColumn;
    @FXML private TableColumn<FeeRecord, String> bookColumn;
    @FXML private TableColumn<FeeRecord, String> borrowDateColumn;
    @FXML private TableColumn<FeeRecord, Integer> feeColumn;
    @FXML private TableColumn<FeeRecord, String> returnDateColumn;

  

    @FXML private Button backButton;
    @FXML private Button dashboardButton;
    @FXML private Button refreshButton;
    @FXML private Label totalLabel;

    private BorrowDAO borrowDAO = new BorrowDAO();

   @FXML
public void initialize() {
    studentColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
    bookColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
    borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
    feeColumn.setCellValueFactory(new PropertyValueFactory<>("lateFee"));
    returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

    backButton.setOnAction(e -> SceneManager.goBack());
    dashboardButton.setOnAction(e -> SceneManager.goToDashboard());
    refreshButton.setOnAction(e -> loadFees());

    loadFees();
}

    private void loadFees() {
        List<FeeRecord> records = borrowDAO.getFeeHistory();
        ObservableList<FeeRecord> rows = FXCollections.observableArrayList(records);
        feesTable.setItems(rows);

        int total = records.stream().mapToInt(FeeRecord::getLateFee).sum();
        totalLabel.setText("Total late fees collected: £" + total);
    }
}