package interfaceS;

import interfaceS.scenes.MainScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Table View Sample");
        stage.setWidth(600);
        stage.setHeight(700);
        stage.setScene(new MainScene(stage).getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}