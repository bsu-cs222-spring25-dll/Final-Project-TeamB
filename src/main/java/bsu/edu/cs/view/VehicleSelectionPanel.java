package bsu.edu.cs.view;


import bsu.edu.cs.model.Vehicle;
import bsu.edu.cs.model.VehicleDatabase;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.List;


public class VehicleSelectionPanel extends VBox {
    private final TextField searchField;
    private final ComboBox<Vehicle> vehicleComboBox;
    private final Label mpgLabel;

    private final VehicleDatabase database;
    private Vehicle selectedVehicle;

    public VehicleSelectionPanel(String title){
        super(10);
        this.setPadding(new Insets(10));

        database = new VehicleDatabase();

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

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
                // Not needed for this use case
                return null;
            }
        });

        mpgLabel = new Label("MPG: N/A");

        loadDefaultVehicles();

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
