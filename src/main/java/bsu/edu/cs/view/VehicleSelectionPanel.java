package bsu.edu.cs.view;


import bsu.edu.cs.model.Vehicle;
import bsu.edu.cs.model.VehicleDatabase;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.List;


public class VehicleSelectionPanel extends VBox {

    private final TextField searchField;
    private final ComboBox<Vehicle> vehicleComboBox;
    private final Label mpgLabel;



    private ToggleGroup inputToggle;
    private final RadioButton databaseRadio;
    private RadioButton manualRadio;
    private final GridPane manualInputPane;
//    private TextField makeField;
//    private TextField modelField;
//    private TextField yearField;
//    private TextField cityMpgField;
//    private TextField highwayMpgField;
    private TextField combinedMpgField;
    private Button applyManualButton;

    private final VehicleDatabase database;
    private Vehicle selectedVehicle;

    public VehicleSelectionPanel(String title){
        super(10);
        this.setPadding(new Insets(10));
        this.getStyleClass().add("vehicle-panel");

        database = new VehicleDatabase();

        Label titleLabel = new Label(title);

        inputToggle = new ToggleGroup();
        databaseRadio = new RadioButton("Select from database");
        manualRadio = new RadioButton("Enter manually");
        databaseRadio.setToggleGroup(inputToggle);
        manualRadio.setToggleGroup(inputToggle);
        databaseRadio.setSelected(true);

        HBox toggleBox = new HBox(20, databaseRadio, manualRadio);
        toggleBox.setPadding(new Insets(5, 0, 5, 0));

        VBox databaseSelectionBox = new VBox(10);

        searchField = new TextField();
        searchField.setPromptText("Search Make or Model");
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e-> searchVehicles());

        vehicleComboBox = new ComboBox<>();
        vehicleComboBox.setPromptText("Select a Vehicle");
        vehicleComboBox.setMaxWidth(Double.MAX_VALUE);

        vehicleComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectedVehicle = newValue;
                        updateMpgLabel();
                    }
                }
        );

        vehicleComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Vehicle vehicle) {
                if (vehicle == null) {
                    return null;
                }
                return vehicle.year + " " + vehicle.make + " " + vehicle.model;
            }
            @Override
            public Vehicle fromString(String string) {
                return null;
            }
        });

        vehicleComboBox.getStyleClass().add("combo-box");

        databaseSelectionBox.getChildren().addAll(searchField,searchButton,vehicleComboBox);

        manualInputPane = new GridPane();
        manualInputPane.setHgap(10);
        manualInputPane.setVgap(5);


        combinedMpgField = new TextField();
        combinedMpgField.setPromptText("Combined MPG");


        manualInputPane.add(new Label("Combined MPG:"), 0, 5);
        manualInputPane.add(combinedMpgField, 1, 5);

        applyManualButton = new Button("Apply");
        manualInputPane.add(applyManualButton, 1, 6);

        manualInputPane.setVisible(false);

        mpgLabel = new Label("MPG: N/A");

        loadDefaultVehicles();

        inputToggle.selectedToggleProperty().addListener((observable, oldValue,newValue) -> {
            if (newValue == databaseRadio) {
                databaseSelectionBox.setVisible(true);
                manualInputPane.setVisible(false);
            } else {
                databaseSelectionBox.setVisible(false);
                manualInputPane.setVisible(true);
            }
        });

        searchButton.getStyleClass().add("button");
        mpgLabel.getStyleClass().add("mpg-value");


        this.getChildren().addAll(
                titleLabel,
                searchField,
                searchButton,
                vehicleComboBox,
                mpgLabel
        );



    }

    private void loadDefaultVehicles() {
        List<Vehicle> vehicles = database.getDefaultVehicles();
        if (vehicles != null && !vehicles.isEmpty()) {
            vehicleComboBox.setItems(FXCollections.observableArrayList(vehicles));
        } else{
            System.out.println("Warning: No vehicles loaded from database");
        }
    }

    private void searchVehicles() {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            List<Vehicle> results = database.searchVehicles(query);
            vehicleComboBox.setItems(FXCollections.observableArrayList(results));
            if (!results.isEmpty()) {
                vehicleComboBox.getSelectionModel().select(0);
                selectedVehicle = vehicleComboBox.getSelectionModel().getSelectedItem();
                updateMpgLabel();
            } else {
                mpgLabel.setText("No matching vehicles found");
            }
        } else {
            loadDefaultVehicles();
        }
    }


    private void updateMpgLabel(){
        if (selectedVehicle != null){
            mpgLabel.setText(String.format("City: %.1f MPG, Highway %.1f MPG, Combined %.1f MPG",
                    selectedVehicle.cityMpg,
                    selectedVehicle.highwayMpg,
                    selectedVehicle.combinedMpg));
        } else{
            mpgLabel.setText("MPG: N/A");
        }
    }
    public Vehicle getSelectedVehicle(){
        return selectedVehicle;
    }
}
