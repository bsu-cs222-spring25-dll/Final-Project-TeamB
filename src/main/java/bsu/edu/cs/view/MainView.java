package bsu.edu.cs.view;

import bsu.edu.cs.model.FuelCalculator;
import bsu.edu.cs.model.Vehicle;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainView extends BorderPane {

    public Vehicle vehicle1;
    public Vehicle vehicle2;
    public FuelCalculator calculator;

    private VehicleSelectionPanel leftPanel;
    private VehicleSelectionPanel rightPanel;
    private ComparisonResultView resultView;

    public MainView(){
        calculator = new FuelCalculator();

        HBox topSection = createTopSection();

        leftPanel = new VehicleSelectionPanel("Vehicle 1");
        rightPanel = new VehicleSelectionPanel("Vehicle 2");

        HBox middleSection  = new HBox(20);
        middleSection.setPadding(new Insets(10));
        middleSection.getChildren().addAll(leftPanel,rightPanel);

        resultView = new ComparisonResultView();

        Button calculateButton = new Button("Compare Vehicles");
        calculateButton.setOnAction(e -> compareVehicles());

        VBox bottomSection = new VBox(10);
        bottomSection.setPadding(new Insets(10));
        bottomSection.getChildren().addAll(calculateButton, resultView);

        this.setTop(topSection);
        this.setCenter(middleSection);
        this.setBottom(bottomSection);
    }

    private HBox createTopSection() {
        HBox topSection = new HBox(20);
        topSection.setPadding(new Insets(10));

        Label gasPriceLabel = new Label("Gas Price ($):");
        TextField gasPriceField = new TextField(String.valueOf(calculator.annualGasPrice));
        gasPriceField.setPrefWidth(80);
        gasPriceField.textProperty().addListener((obs,oldVal,newVal) -> {
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

        topSection.getChildren().addAll(gasPriceLabel,gasPriceField,milesLabel,milesField);
        return topSection;
    }

    private void compareVehicles(){
        vehicle1 = leftPanel.getSelectedVehicle();
        vehicle2 = rightPanel.getSelectedVehicle();

        if (vehicle1 != null && vehicle2 != null){
            double annualCost1 = calculator.calculateAnnualFuelCost(vehicle1);
            double annualCost2 = calculator.calculateAnnualFuelCost(vehicle2);
            double savings = calculator.calculateOneYearSavings(vehicle1,vehicle2);
            double fiveYearSavings = calculator.calculateFiveYearSavings(vehicle1,vehicle2);
            String moreEfficient = calculator.getMoreEfficientVehicle(vehicle1,vehicle2);

            resultView.updateResults(vehicle1,vehicle2,annualCost1,annualCost2,savings,fiveYearSavings,moreEfficient);
        } else{
            resultView.showError("Please select two vehicles to compare");
        }
    }
}
