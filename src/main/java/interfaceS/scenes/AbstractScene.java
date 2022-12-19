package interfaceS.scenes;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public abstract class AbstractScene {
    protected final Stage stage;

    public AbstractScene(Stage stage) {
        this.stage = stage;
    }

    protected void showErrorMessage(String header, String text) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(text);
        errorAlert.showAndWait();
    }
}
