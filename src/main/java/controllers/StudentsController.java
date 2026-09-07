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
    @FXML private Button editStudentButton;
    @FXML private Button deleteStudentButton;
    @FXML private Button refreshButton;
    @FXML private Button backButton;
    @FXML private Button dashboardButton;

    private StudentDAO studentDAO = new StudentDAO();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        addStudentButton.setOnAction(e -> handleAddStudent());
        editStudentButton.setOnAction(e -> handleEditStudent());
        deleteStudentButton.setOnAction(e -> handleDeleteStudent());
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
        Dialog<Student> dialog = buildStudentDialog("Add Student", null);
        Optional<Student> result = dialog.showAndWait();
        result.ifPresent(student -> {
            if (!student.getName().isBlank()) {
                studentDAO.addStudent(student);
                loadStudents();
            }
        });
    }

    private void handleEditStudent() {
        Student selected = studentsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No student selected", "Please select a student to edit.");
            return;
        }

        Dialog<Student> dialog = buildStudentDialog("Edit Student", selected);
        Optional<Student> result = dialog.showAndWait();
        result.ifPresent(updatedStudent -> {
            if (!updatedStudent.getName().isBlank()) {
                studentDAO.updateStudent(updatedStudent);
                loadStudents();
            }
        });
    }

    private void handleDeleteStudent() {
        Student selected = studentsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No student selected", "Please select a student to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Student");
        confirm.setHeaderText("Delete \"" + selected.getName() + "\"?");
        confirm.setContentText("This cannot be undone.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            studentDAO.deleteStudent(selected.getStudentID());
            loadStudents();
        }
    }

    private Dialog<Student> buildStudentDialog(String title, Student existing) {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(existing == null ? "Enter the new student's details" : "Update the student's details");

        ButtonType confirmButtonType = new ButtonType(existing == null ? "Add" : "Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");

        if (existing != null) {
            nameField.setText(existing.getName());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == confirmButtonType) {
                int id = existing == null ? 0 : existing.getStudentID();
                return new Student(id, nameField.getText());
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