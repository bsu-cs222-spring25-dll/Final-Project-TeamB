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

    private final RadioButton databaseRadio;
    private final GridPane manualInputPane;
    private final TextField combinedMpgField;
    private final TextField makeField;
    private final TextField modelField;
    private final TextField yearField;

    private final VehicleDatabase database;
    private Vehicle selectedVehicle;

    public VehicleSelectionPanel(String title){
        super(10);
        this.setPadding(new Insets(10));
        this.getStyleClass().add("vehicle-panel");

        database = new VehicleDatabase();

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("panel-title");

        ToggleGroup inputToggle = new ToggleGroup();
        databaseRadio = new RadioButton("Select from database");
        RadioButton manualRadio = new RadioButton("Enter manually");
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
                return vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel();
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

        makeField = new TextField();
        makeField.setPromptText("Make (e.g. Toyota)");

        modelField = new TextField();
        modelField.setPromptText("Model (e.g. Camry)");

        yearField = new TextField();
        yearField.setPromptText("Year (e.g. 2023)");

        combinedMpgField = new TextField();
        combinedMpgField.setPromptText("Combined MPG");

        manualInputPane.add(new Label("Make:"), 0, 0);
        manualInputPane.add(makeField, 1, 0);
        manualInputPane.add(new Label("Model:"), 0, 1);
        manualInputPane.add(modelField, 1, 1);
        manualInputPane.add(new Label("Year:"), 0, 2);
        manualInputPane.add(yearField, 1, 2);
        manualInputPane.add(new Label("Combined MPG:"), 0, 3);
        manualInputPane.add(combinedMpgField, 1, 3);

        Button applyManualButton = new Button("Apply");
        applyManualButton.setOnAction(e -> applyManualEntry());
        manualInputPane.add(applyManualButton, 1, 4);

        manualInputPane.setVisible(false);

        mpgLabel = new Label("MPG: N/A");

        loadDefaultVehicles();


        inputToggle.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
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
                toggleBox,
                databaseSelectionBox,
                manualInputPane,
                mpgLabel
        );



    }

    private void loadDefaultVehicles() {
        List<Vehicle> vehicles = database.getVehicles();
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

    private void applyManualEntry() {
        try {
            String make = makeField.getText().trim();
            String model = modelField.getText().trim();
            double mpg = Double.parseDouble(combinedMpgField.getText().trim());
            //int year = Integer.parseInt(yearField.getText().trim());

            if (make.isEmpty() || model.isEmpty()) {
                mpgLabel.setText("Please enter both make and model");
                return;
            }

            selectedVehicle = new Vehicle(make, model, mpg);
            updateMpgLabel();

        } catch (NumberFormatException e) {
            mpgLabel.setText("Please enter valid numeric values for MPG and year");
        }
    }

    private void updateMpgLabel(){
        if (selectedVehicle != null){
            mpgLabel.setText(String.format("City: %.1f MPG, Highway %.1f MPG, Combined %.1f MPG",
                    selectedVehicle.getCityMpg(),
                    selectedVehicle.getHighwayMpg(),
                    selectedVehicle.getCombinedMpg()));
        } else{
            mpgLabel.setText("MPG: N/A");
        }
    }

    public Vehicle getSelectedVehicle(){
        return selectedVehicle;
    }
}