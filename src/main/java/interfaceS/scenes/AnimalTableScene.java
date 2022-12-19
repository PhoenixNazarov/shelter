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
import shelter.database.models.Animal;
import shelter.database.models.Category;
import shelter.database.service.AnimalService;
import shelter.database.service.CategoryService;
import shelter.database.service.PlaceService;

public class AnimalTableScene extends AbstractScene {
    private TableView table = new TableView();
    final HBox hb = new HBox();

    private final AnimalService animalService = new AnimalService();
    private final PlaceService placeService = new PlaceService();
    private final CategoryService categoryService = new CategoryService();


    public AnimalTableScene(Stage stage) {
        super(stage);
    }

    public Scene getScene() {
        Scene scene = new Scene(new Group());

        final Label label = new Label("Animals");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);
        table.setMinWidth(300);

        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Animal, String>("name"));

        TableColumn categoryNameCol = new TableColumn("Category name");
        categoryNameCol.setCellValueFactory(
                new PropertyValueFactory<Animal, String>("categoryName"));

        TableColumn placeNumberCol = new TableColumn("Place number");
        placeNumberCol.setCellValueFactory(
                new PropertyValueFactory<Animal, Integer>("placeNumber"));


        TableColumn descriptionCol = new TableColumn("Description");
        descriptionCol.setCellValueFactory(
                new PropertyValueFactory<Animal, String>("description"));


        TableColumn vaccinatedCol = new TableColumn("Vaccinated");
        vaccinatedCol.setCellValueFactory(
                new PropertyValueFactory<Animal, Boolean>("vaccinated"));


        TableColumn timeStartCol = new TableColumn("Time start");
        timeStartCol.setCellValueFactory(
                new PropertyValueFactory<Animal, String>("timeStart"));


//        TableColumn timeEndCol = new TableColumn("Time end");
//        timeEndCol.setCellValueFactory(
//                new PropertyValueFactory<Animal, String>("timeEnd"));


        table.getColumns().addAll(nameCol, categoryNameCol, placeNumberCol, descriptionCol, vaccinatedCol, timeStartCol);

        ObservableList<Animal> animals = FXCollections.observableArrayList(animalService.getAnimals());

        table.setItems(animals);
        addButtonToTable();
        addButtonToTableVaccinate();

        final TextField addName = new TextField();
        addName.setPromptText("Name");
        addName.setMaxWidth(addName.getPrefWidth());

        final TextField descriptionAdd = new TextField();
        descriptionAdd.setPromptText("Description");
        descriptionAdd.setMaxWidth(descriptionAdd.getPrefWidth());


        ObservableList<String> categories = FXCollections.observableArrayList(categoryService.getNames());
        ComboBox<String> categoryComboBox = new ComboBox<>(categories);

        ObservableList<Integer> places = FXCollections.observableArrayList(placeService.getNumbers());
        ComboBox<Integer> numberComboBox = new ComboBox<>(places);


        final Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            Animal animal = new Animal();

            animal.setCategoryName(categoryComboBox.getValue());
            animal.setPlaceNumber(numberComboBox.getValue());
            animal.setName(addName.getText());
            animal.setDescription(descriptionAdd.getText());
            try {
                animalService.createAnimal(animal);
                stage.setScene(new AnimalTableScene(stage).getScene());

            } catch (ValidationException x) {
                showErrorMessage("validation error", x.getMessage());
            }
        });

        final Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            stage.setScene(new MainScene(stage).getScene());
        });

        hb.getChildren().addAll(addName, descriptionAdd, categoryComboBox, numberComboBox, addButton);
        hb.setSpacing(3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(backButton, label, table, hb);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        return scene;
    }

    private void addButtonToTable() {
        TableColumn<Animal, Void> colBtn = new TableColumn("Delete Column");

        Callback<TableColumn<Animal, Void>, TableCell<Animal, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Animal, Void> call(final TableColumn<Animal, Void> param) {
                final TableCell<Animal, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Delete");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Animal data = getTableView().getItems().get(getIndex());
                            animalService.delete(data);
                            stage.setScene(new AnimalTableScene(stage).getScene());
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

    private void addButtonToTableVaccinate() {
        TableColumn<Animal, Void> colBtn = new TableColumn("Vaccinate Column");

        Callback<TableColumn<Animal, Void>, TableCell<Animal, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Animal, Void> call(final TableColumn<Animal, Void> param) {

                final TableCell<Animal, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Vaccinate");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Animal data = getTableView().getItems().get(getIndex());
                            animalService.setVaccinate(data, true);
                            stage.setScene(new AnimalTableScene(stage).getScene());
                        });
                    }

                    private final Button btn2 = new Button("UnVaccinate");
                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            Animal data = getTableView().getItems().get(getIndex());
                            animalService.setVaccinate(data, false);
                            stage.setScene(new AnimalTableScene(stage).getScene());
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Animal data = getTableView().getItems().get(getIndex());
                            if (!data.getVaccinated()) {
                                setGraphic(btn);
                            } else {
                                setGraphic(btn2);
                            }
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