package bsu.edu.cs.view;

import bsu.edu.cs.model.FuelCalculator;
import bsu.edu.cs.model.Vehicle;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainView extends BorderPane {

    private Vehicle vehicle1;
    private Vehicle vehicle2;
    private final FuelCalculator calculator;
    private TextField mpg1Field;
    private TextField mpg2Field;
    private TextField gasPriceField;
    private TextField milesField;
    private TextField timeField;

    private final VehicleSelectionPanel leftPanel;
    private final VehicleSelectionPanel rightPanel;
    private final ComparisonResultView resultView;

    public MainView() throws IOException {
        calculator = new FuelCalculator();

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
        calculateButton.setOnAction(e -> compareVehicles());

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
        gasPriceField = new TextField(String.valueOf(calculator.getAnnualGasPrice()));
        gasPriceField.setPrefWidth(80);

        Label milesLabel = new Label("Annual Miles:");
        milesField = new TextField(String.valueOf(calculator.getAnnualMiles()));
        milesField.setPrefWidth(100);

        Label timeLabel = new Label("Number of Years: ");
        timeField = new TextField(String.valueOf(calculator.getYearsOwned()));
        timeField.setPrefWidth(80);

        Button recalculateButton = new Button("Recalculate");
        recalculateButton.setOnAction(e -> recalculateCheck());

        topSection.getChildren().addAll(gasPriceLabel, gasPriceField,
                milesLabel,milesField,
                timeLabel, timeField,
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

                int currentYear = 2025;
                vehicle1 = new Vehicle("Vehicle","1", mpg1, currentYear);
                vehicle2 = new Vehicle("Vehicle","2", mpg2, currentYear);

                performComparison();
            } catch (NumberFormatException e) {
                resultView.showError("Please enter valid MPG values");
            }
        } else {
            vehicle1 = leftPanel.getSelectedVehicle();
            vehicle2 = rightPanel.getSelectedVehicle();

            if (vehicle1 != null && vehicle2 != null) {
                performComparison();
            } else {
                resultView.showError("Please select two vehicles to compare or enter MPG values directly");
            }
        }
    }

    private void performComparison() {
        double annualCost1 = calculator.calculateAnnualFuelCost(vehicle1);
        double annualCost2 = calculator.calculateAnnualFuelCost(vehicle2);
        double yearCost1 = calculator.calculateYearsOwnedFuelCost(vehicle1);
        double yearCost2 = calculator.calculateYearsOwnedFuelCost(vehicle2);
        double savings = calculator.calculateOneYearSavings(vehicle1, vehicle2);
        double yearSavings = calculator.calculateYearSavings(vehicle1, vehicle2);
        String moreEfficient = calculator.getMoreEfficientVehicle(vehicle1, vehicle2);

        resultView.updateResults(vehicle1, vehicle2, annualCost1, annualCost2, yearCost1,yearCost2, savings, calculator.getYearsOwned(), yearSavings, moreEfficient);
    }

    private void recalculateCheck() {
        try {
            double gasPrice = Double.parseDouble(gasPriceField.getText());
            int miles = Integer.parseInt(milesField.getText());
            int years = Integer.parseInt(timeField.getText());

            if (gasPrice <= 0 || miles <= 0 || years <= 0) {
                resultView.showError("Values must be greater than zero");
                return;
            }

            calculator.setAnnualGasPrice(gasPrice);
            calculator.setAnnualMiles(miles);
            calculator.setYearsOwned(years);

            resultView.showSuccess("Values updated successfully!");

        } catch (NumberFormatException e) {
            resultView.showError("Please enter valid numbers!");
        }

    }
}