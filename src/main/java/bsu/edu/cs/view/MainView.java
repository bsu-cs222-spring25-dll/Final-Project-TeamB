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

public class MainView extends BorderPane {

    public Vehicle vehicle1;
    public Vehicle vehicle2;
    public FuelCalculator calculator;
    private TextField mpg1Field;
    private TextField mpg2Field;
    public double yearsOwned;

    private final VehicleSelectionPanel leftPanel;
    private final VehicleSelectionPanel rightPanel;
    private final ComparisonResultView resultView;

    public MainView(){
        calculator = new FuelCalculator();

        HBox topSection = createTopSection();

        leftPanel = new VehicleSelectionPanel("Vehicle 1");
        rightPanel = new VehicleSelectionPanel("Vehicle 2");

        HBox middleSection  = new HBox(20);
        middleSection.setPadding(new Insets(10));
        middleSection.getChildren().addAll(leftPanel,rightPanel);

        resultView = new ComparisonResultView();

        GridPane directMpgPanel = createDirectMpgPanel();

        Button calculateButton = new Button("Compare Vehicles");
        calculateButton.setOnAction(e -> compareVehicles());

        VBox bottomSection = new VBox(10);
        bottomSection.setPadding(new Insets(10));
        bottomSection.getChildren().addAll(directMpgPanel,calculateButton, resultView);

        this.setTop(topSection);
        this.setCenter(middleSection);
        this.setBottom(bottomSection);
    }

    private HBox createTopSection() {
        HBox topSection = new HBox(20);
        topSection.setPadding(new Insets(10));
        topSection.getStyleClass().add("top-section");

        Label gasPriceLabel = new Label("Gas Price ($):");
        TextField gasPriceField = new TextField(String.valueOf(calculator.annualGasPrice));
        gasPriceField.setPrefWidth(80);
        gasPriceField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                calculator.annualGasPrice = Double.parseDouble(newVal);
            } catch (NumberFormatException e){
                gasPriceField.setText(oldVal);
            }
        });
        Label milesLabel = new Label("Annual Miles:");
        TextField milesField = new TextField(String.valueOf(calculator.annualMiles));
        milesField.setPrefWidth(100);
        milesField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                calculator.annualMiles = Integer.parseInt(newVal);
            } catch (NumberFormatException e) {
                milesField.setText(oldVal);
            }
        });
        Label timeLabel = new Label("Number of Years: ");
        TextField timeField = new TextField(String.valueOf(calculator.yearsOwned));
        timeField.setPrefWidth(80);
        timeField.textProperty().addListener((obs, oldVal, newVal) -> {
            try{
                calculator.yearsOwned = Integer.parseInt(newVal);
            } catch (NumberFormatException e){
                timeField.setText(oldVal);
            }
        });

        topSection.getChildren().addAll(gasPriceLabel,gasPriceField,milesLabel,milesField, timeLabel, timeField);
        return topSection;
    }

    private GridPane createDirectMpgPanel(){
        GridPane panel = new GridPane();
        panel.setHgap(10);
        panel.setVgap(10);
        panel.setPadding(new Insets(10));
        panel.getStyleClass().add("mpg-input-panel");

        Label directInputLabel = new Label("Or directly compare MPG values: ");
        directInputLabel.setStyle("-fx-font-weight: bold;");
        panel.add(directInputLabel,0,0,2,1);

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
    private void compareVehicles(){
        boolean usingDirectMpg = !mpg1Field.getText().trim().isEmpty() && !mpg2Field.getText().trim().isEmpty();

        if (usingDirectMpg) {

            try {
                double mpg1 = Double.parseDouble(mpg1Field.getText().trim());
                double mpg2 = Double.parseDouble(mpg2Field.getText().trim());


                vehicle1 = new Vehicle("Vehicle","1", mpg1, yearsOwned);
                vehicle2 = new Vehicle("Vehicle","2", mpg2, yearsOwned);

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
        double savings = calculator.calculateOneYearSavings(vehicle1, vehicle2);
        double YearSavings = calculator.calculateYearSavings(vehicle1, vehicle2);
        String moreEfficient = calculator.getMoreEfficientVehicle(vehicle1, vehicle2);

        resultView.updateResults(vehicle1, vehicle2, annualCost1, annualCost2, savings, calculator.yearsOwned, YearSavings, moreEfficient);
    }
}
