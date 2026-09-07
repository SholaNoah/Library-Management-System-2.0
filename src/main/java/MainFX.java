import javafx.application.Application;
import javafx.stage.Stage;
import utils.SceneManager;
import utils.DatabaseServiceManager;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        DatabaseServiceManager.ensureRunning();

        SceneManager.setStage(stage);
        SceneManager.goToDashboard();
        stage.setTitle("Library Management System");
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}