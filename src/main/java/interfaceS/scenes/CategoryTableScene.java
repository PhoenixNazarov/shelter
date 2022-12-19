package interfaceS.scenes;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import shelter.database.exception.ValidationException;
import shelter.database.models.Category;
import shelter.database.service.CategoryService;

public class CategoryTableScene extends AbstractScene{
    private TableView table = new TableView();
    final HBox hb = new HBox();

    private final CategoryService categoryService = new CategoryService();

    public CategoryTableScene(Stage stage) {
        super(stage);
    }

    public Scene getScene() {
        Scene scene = new Scene(new Group());

        final Label label = new Label("Animal categories");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);
        table.setMinWidth(300);

        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(150);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Category, String>("name"));

        table.getColumns().addAll(nameCol);

        ObservableList<Category> categories = FXCollections.observableArrayList(categoryService.getCategories());

        table.setItems(categories);
        addButtonToTable();

        final TextField addName = new TextField();
        addName.setPromptText("Name");
        addName.setMaxWidth(addName.getPrefWidth());

        final Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            Category category = new Category();
            category.setName(addName.getText());
            try {
                categoryService.createCategory(category);
                stage.setScene(new CategoryTableScene(stage).getScene());
            } catch (ValidationException x) {
                showErrorMessage("validation error", x.getMessage());
            }
        });

        final Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            stage.setScene(new MainScene(stage).getScene());
        });

        hb.getChildren().addAll(addName, addButton);
        hb.setSpacing(3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(backButton, label, table, hb);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        return scene;
    }

    private void addButtonToTable() {
        TableColumn<Category, Void> colBtn = new TableColumn("Button Column");

        Callback<TableColumn<Category, Void>, TableCell<Category, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Category, Void> call(final TableColumn<Category, Void> param) {
                final TableCell<Category, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Delete");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Category data = getTableView().getItems().get(getIndex());
                            categoryService.delete(data);
                            stage.setScene(new CategoryTableScene(stage).getScene());
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