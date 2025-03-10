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

import java.util.List;


public class VehicleSelectionPanel extends VBox {
    private Label titleLabel;
    private TextField searchField;
    private Button searchButton;
    private ComboBox<Vehicle> vehicleComboBox;
    private Label mpgLabel;

    private VehicleDatabase database;
    private Vehicle selectedVehicle;

    public VehicleSelectionPanel(String title){
        super(10);
        this.setPadding(new Insets(10));

        database = new VehicleDatabase();

        titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        searchField = new TextField();
        searchField.setPromptText("Search Make or Model");
        searchButton = new Button("Search");
        searchButton.setOnAction(e-> searchVehicles());

        vehicleComboBox = new ComboBox<>();
        vehicleComboBox.setPromptText("Select a Vehicle");
        vehicleComboBox.setMaxWidth(Double.MAX_VALUE);
        vehicleComboBox.setOnAction(e-> updateSelectedVehicle());

        mpgLabel = new Label("MPG: N/A");

        loadDefaultVehicles();

    }

    private void loadDefaultVehicles() {
        List<Vehicle> vehicles = database.getDefaultVehicles();
        vehicleComboBox.setItems(FXCollections.observableArrayList(vehicles));
    }

    private void searchVehicles() {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            List<Vehicle> results = database.searchVehicles(query);
            vehicleComboBox.setItems(FXCollections.observableArrayList(results));
            if (!results.isEmpty()){
                vehicleComboBox.getSelectionModel().selectFirst();
                updateSelectedVehicle();
            }
        } else{
            loadDefaultVehicles();
        }
    }
    private void updateSelectedVehicle(){
        selectedVehicle = vehicleComboBox.getSelectionModel().getSelectedItem();
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
