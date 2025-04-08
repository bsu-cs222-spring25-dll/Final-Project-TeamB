package bsu.edu.cs.view;

import bsu.edu.cs.controller.FuelComparisonController;
import bsu.edu.cs.model.ComparisonResult;
import bsu.edu.cs.model.Vehicle;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainView extends BorderPane {
    private final FuelComparisonController controller;
    private Vehicle vehicle1;
    private Vehicle vehicle2;
    private TextField mpg1Field;
    private TextField mpg2Field;
    private TextField gasPriceField;
    private TextField milesField;
    private TextField timeField;
    private TextField electricField;
    private final VehicleSelectionPanel leftPanel;
    private final VehicleSelectionPanel rightPanel;
    private final ComparisonResultView resultView;

    public MainView(FuelComparisonController controller) throws Exception {
        this.controller = controller;

        String csvFilePath = "src/main/resources/vehicles.csv";

        HBox topSection = createTopSection();
        leftPanel = new VehicleSelectionPanel("Vehicle 1", csvFilePath);
        rightPanel = new VehicleSelectionPanel("Vehicle 2", csvFilePath);

        HBox middleSection = new HBox(20);
        middleSection.setPadding(new Insets(10));
        middleSection.getChildren().addAll(leftPanel, rightPanel);

        resultView = new ComparisonResultView();
        GridPane directMpgPanel = createDirectMpgPanel();

        Button calculateButton = new Button("Compare Vehicles");
        calculateButton.setOnAction(_ -> compareVehicles());

        VBox bottomSection = new VBox(10);
        bottomSection.setPadding(new Insets(10));
        bottomSection.getChildren().addAll( directMpgPanel, calculateButton, resultView);

        this.setTop(topSection);
        this.setCenter(middleSection);
        this.setBottom(bottomSection);
    }

    private HBox createTopSection() {
        HBox topSection = new HBox(20);
        topSection.setPadding(new Insets(10));
        topSection.getStyleClass().add("top-section");

        Label gasPriceLabel = new Label("Gas Price ($):");
        gasPriceField = new TextField(String.valueOf(3.50));
        gasPriceField.setPrefWidth(80);

        Label milesLabel = new Label("Annual Miles:");
        milesField = new TextField(String.valueOf(15000));
        milesField.setPrefWidth(80);

        Label timeLabel = new Label("Estimated Length of Ownership: ");
        timeField = new TextField(String.valueOf(5));
        timeField.setPrefWidth(80);

        Label electricityLabel = new Label("Electricity: ($/kwh)");
        electricField = new TextField(String.valueOf(0.13));
        electricField.setPrefWidth(80);

        Button recalculateButton = new Button("Recalculate");
        recalculateButton.setOnAction(_ -> recalculateCheck());

        topSection.getChildren().addAll(gasPriceLabel, gasPriceField,
                milesLabel, milesField,
                timeLabel, timeField,
                electricityLabel, electricField,
                recalculateButton);
        return topSection;
    }

    private GridPane createDirectMpgPanel() {
        GridPane panel = new GridPane();
        panel.setHgap(10);
        panel.setVgap(10);
        panel.setPadding(new Insets(10));
        panel.getStyleClass().add("mpg-input-panel");

        Label directInputLabel = new Label("Or directly compare MPG values: ");
        directInputLabel.setStyle("-fx-font-weight: bold;");

        panel.add(directInputLabel, 0, 0, 2, 1);

        Label vehicle1Label = new Label("Vehicle 1 MPG:");
        mpg1Field = new TextField();
        mpg1Field.setPromptText("Enter MPG");
        mpg1Field.setPrefWidth(100);

        Label vehicle2Label = new Label("Vehicle 2 MPG:");
        mpg2Field = new TextField();
        mpg2Field.setPromptText("Enter MPG");
        mpg2Field.setPrefWidth(100);

        panel.add(vehicle1Label, 0, 1);
        panel.add(mpg1Field, 1, 1);
        panel.add(vehicle2Label, 0, 2);
        panel.add(mpg2Field, 1, 2);

        return panel;
    }

    private void compareVehicles() {
        boolean usingDirectMpg = !mpg1Field.getText().trim().isEmpty() && !mpg2Field.getText().trim().isEmpty();

        if (usingDirectMpg) {
            try {
                double mpg1 = Double.parseDouble(mpg1Field.getText().trim());
                double mpg2 = Double.parseDouble(mpg2Field.getText().trim());

                if (mpg1 > 150 || mpg2 > 150) {
                    resultView.showError("MPG values must be 150 or below");
                    return;
                }

                vehicle1 = controller.createVehicleFromMpg(mpg1, "1");
                vehicle2 = controller.createVehicleFromMpg(mpg2, "2");
                updateResults();
            } catch (NumberFormatException e) {
                resultView.showError("Please enter valid MPG values");
            }
        } else {
            vehicle1 = leftPanel.getSelectedVehicle();
            vehicle2 = rightPanel.getSelectedVehicle();

            if (vehicle1 != null && vehicle2 != null) {
                updateResults();
            } else {
                resultView.showError("Please select two vehicles to compare or enter MPG values directly");
            }
        }
    }

    private void updateResults() {
        ComparisonResult result = controller.compareVehicles(vehicle1, vehicle2);
        resultView.updateResults(result);
    }

    private void recalculateCheck() {
        try {
            double gasPrice = Double.parseDouble(gasPriceField.getText());
            int miles = Integer.parseInt(milesField.getText());
            int years = Integer.parseInt(timeField.getText());
            double electricPrice = Double.parseDouble(electricField.getText());

            if (gasPrice <= 0 || miles <= 0 || years <= 0 || electricPrice <= 0) {
                resultView.showError("Values must be greater than zero");
                return;
            }

            controller.updateCalculatorSettings(gasPrice, miles, years, electricPrice);
            resultView.showSuccess("Values updated successfully!");

        } catch (NumberFormatException e) {
            resultView.showError("Please enter valid numbers!");
        }
    }
}