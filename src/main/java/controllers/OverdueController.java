package controllers;

import dao.BorrowDAO;
import dao.BorrowDAO.OverdueRecord;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.SceneManager;

public class OverdueController {

    @FXML private TableView<OverdueRecord> overdueTable;
    @FXML private TableColumn<OverdueRecord, String> studentColumn;
    @FXML private TableColumn<OverdueRecord, String> bookColumn;
    @FXML private TableColumn<OverdueRecord, String> dueDateColumn;
    @FXML private TableColumn<OverdueRecord, Integer> daysLateColumn;
    @FXML private TableColumn<OverdueRecord, Integer> estimatedFeeColumn;

    @FXML private Button backButton;
    @FXML private Button dashboardButton;
    @FXML private Button refreshButton;

    private BorrowDAO borrowDAO = new BorrowDAO();

    @FXML
    public void initialize() {
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        bookColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        daysLateColumn.setCellValueFactory(new PropertyValueFactory<>("daysLate"));
        estimatedFeeColumn.setCellValueFactory(new PropertyValueFactory<>("estimatedFee"));

        backButton.setOnAction(e -> SceneManager.goBack());
        dashboardButton.setOnAction(e -> SceneManager.goToDashboard());
        refreshButton.setOnAction(e -> loadOverdue());

        loadOverdue();
    }

    private void loadOverdue() {
        ObservableList<OverdueRecord> rows = FXCollections.observableArrayList(borrowDAO.getOverdueBooks());
        overdueTable.setItems(rows);
    }
}