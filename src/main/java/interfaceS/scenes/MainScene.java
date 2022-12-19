package interfaceS.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import shelter.database.models.Animal;
import shelter.database.models.Category;
import shelter.database.service.AnimalService;
import shelter.database.service.CategoryService;

public class MainScene extends AbstractScene{
    HBox hb = new HBox();

    AnimalService animalService = new AnimalService();

    public MainScene(Stage stage) {
        super(stage);
    }

    public Scene getScene() {
        Scene scene = new Scene(new Group());

        final Button goCategoryButton = new Button("Change Categories");
        goCategoryButton.setOnAction(e -> {
            stage.setScene(new CategoryTableScene(stage).getScene());
        });

        final Button goPlaceButton = new Button("Change Places");
        goPlaceButton.setOnAction(e -> {
            stage.setScene(new PlaceTableScene(stage).getScene());
        });

        hb.getChildren().addAll(goCategoryButton, goPlaceButton);
        hb.setSpacing(3);


        final Button goAnimalButton = new Button("Change Animals");
        goAnimalButton.setOnAction(e -> {
            stage.setScene(new AnimalTableScene(stage).getScene());
        });

        Label animals = new Label("Total animals: " + animalService.getAnimals().size());
        Label animalsV = new Label("Total animals vaccinated: " + animalService.getAnimals().stream().filter(Animal::getVaccinated).count());

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(hb, goAnimalButton, animals, animalsV);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);


        return scene;
    }
}