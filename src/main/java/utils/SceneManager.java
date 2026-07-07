package utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Stack;

public class SceneManager {

    private static Stage stage;
    private static Scene scene;
    private static Stack<String> history = new Stack<>();
    private static final String STYLESHEET = "/css/styles.css";

    public static void setStage(Stage s) {
        stage = s;
    }

    public static void switchTo(String fxmlPath) {
        navigate(fxmlPath, true);
    }

    public static void goBack() {
        if (history.size() > 1) {
            history.pop();
            String previous = history.peek();
            navigate(previous, false);
        } else {
            goToDashboard();
        }
    }

    public static void goToDashboard() {
        history.clear();
        navigate("/fxml/dashboard.fxml", true);
    }

    private static void navigate(String fxmlPath, boolean recordHistory) {
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource(fxmlPath));

            if (scene == null) {
                // First load: create the one Scene the app will ever use
                scene = new Scene(root);
                scene.getStylesheets().add(SceneManager.class.getResource(STYLESHEET).toExternalForm());
                stage.setScene(scene);
            } else {
                // Every subsequent screen: just swap the content, keep the same Scene
                scene.setRoot(root);
            }

            if (recordHistory) {
                history.push(fxmlPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}