package interfaceS.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import shelter.database.exception.ValidationException;
import shelter.database.models.Category;
import shelter.database.models.Place;
import shelter.database.service.CategoryService;
import shelter.database.service.PlaceService;

public class PlaceTableScene extends AbstractScene {
    private TableView table = new TableView();
    final HBox hb = new HBox();

    private final PlaceService placeService = new PlaceService();

    public PlaceTableScene(Stage stage) {
        super(stage);
    }

    public Scene getScene() {
        Scene scene = new Scene(new Group());

        final Label label = new Label("Animal places");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);
        table.setMinWidth(300);

        TableColumn numberCol = new TableColumn("Number");
        numberCol.setCellValueFactory(new PropertyValueFactory<Place, Integer>("number"));

        TableColumn descriptionCol = new TableColumn("Description");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Place, String>("description"));

        TableColumn maxAnimalCol = new TableColumn("Max Animal");
        maxAnimalCol.setCellValueFactory(new PropertyValueFactory<Place, Integer>("maxAnimal"));

        table.getColumns().addAll(numberCol, descriptionCol, maxAnimalCol);

        ObservableList<Place> places = FXCollections.observableArrayList(placeService.getPlaces());

        table.setItems(places);
        addButtonToTable();

        final TextField numberAdd = new TextField();
        numberAdd.setPromptText("Number");
        numberAdd.setMaxWidth(numberAdd.getPrefWidth());

        final TextField descriptionAdd = new TextField();
        descriptionAdd.setPromptText("Description");
        descriptionAdd.setMaxWidth(descriptionAdd.getPrefWidth());

        final TextField maxAnimalAdd = new TextField();
        maxAnimalAdd.setPromptText("Max animal");
        maxAnimalAdd.setMaxWidth(maxAnimalAdd.getPrefWidth());

        final Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            try {
                Place place = new Place();
                place.setNumber(Integer.parseInt(numberAdd.getText()));
                place.setDescription(descriptionAdd.getText());
                try {
                    place.setMaxAnimal(Integer.parseInt(numberAdd.getText()));
                    placeService.createPlace(place);
                    stage.setScene(new PlaceTableScene(stage).getScene());
                } catch (NumberFormatException xx) {
                    showErrorMessage("value error", "max animal should be integer");
                } catch (ValidationException ex) {
                    showErrorMessage("value error", ex.getMessage());
                }
            } catch (NumberFormatException x) {
                showErrorMessage("value error", "number should be integer");
            }
        });

        final Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            stage.setScene(new MainScene(stage).getScene());
        });

        hb.getChildren().addAll(numberAdd, descriptionAdd, maxAnimalAdd, addButton);
        hb.setSpacing(3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(backButton, label, table, hb);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        return scene;
    }

    private void addButtonToTable() {
        TableColumn<Place, Void> colBtn = new TableColumn("Button Column");

        Callback<TableColumn<Place, Void>, TableCell<Place, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Place, Void> call(final TableColumn<Place, Void> param) {
                final TableCell<Place, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Delete");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Place data = getTableView().getItems().get(getIndex());
                            placeService.delete(data);
                            stage.setScene(new PlaceTableScene(stage).getScene());
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);

        table.getColumns().add(colBtn);
    }
}