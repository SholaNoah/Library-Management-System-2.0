package controllers;

import dao.StudentDAO;
import models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;
import utils.SceneManager;
import java.util.Optional;

public class StudentsController {

    @FXML private TableView<Student> studentsTable;
    @FXML private TableColumn<Student, Integer> idColumn;
    @FXML private TableColumn<Student, String> nameColumn;

    @FXML private Button addStudentButton;
    @FXML private Button refreshButton;
    @FXML private Button backButton;
    @FXML private Button dashboardButton;

    private StudentDAO studentDAO = new StudentDAO();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        addStudentButton.setOnAction(e -> handleAddStudent());
        refreshButton.setOnAction(e -> loadStudents());
        backButton.setOnAction(e -> SceneManager.goBack());
        dashboardButton.setOnAction(e -> SceneManager.goToDashboard());

        loadStudents();
    }

    private void loadStudents() {
        ObservableList<Student> students = FXCollections.observableArrayList(studentDAO.getAllStudents());
        studentsTable.setItems(students);
    }

    private void handleAddStudent() {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Add Student");
        dialog.setHeaderText("Enter the new student's details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButtonType) {
                return new Student(0, nameField.getText());
            }
            return null;
        });

        Optional<Student> result = dialog.showAndWait();
        result.ifPresent(student -> {
            if (!student.getName().isBlank()) {
                studentDAO.addStudent(student);
                loadStudents();
            }
        });
    }
}