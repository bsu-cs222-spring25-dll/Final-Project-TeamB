package bsu.edu.cs.view;

import bsu.edu.cs.controller.FuelComparisonController;
import bsu.edu.cs.controller.FuelComparisonControllerImpl;
import bsu.edu.cs.model.ComparisonResult;
import bsu.edu.cs.model.FuelCalculator;
import bsu.edu.cs.model.Vehicle;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

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
    private final FinancialSettingPane financialSettingsPane;
    private final FuelCalculator calculator;

    public MainView(FuelComparisonController controller) {
        this.controller = controller;
        this.financialSettingsPane = new FinancialSettingPane();
        this.calculator = new FuelCalculator();
        financialSettingsPane.getRecalculateButton().setOnAction(_ -> recalculateFinancials());

        VBox header = createInstructionHeader();
        Accordion vehicleHabits = createVehicleHabits();
        leftPanel = new VehicleSelectionPanel("Vehicle 1", controller, this::onVehicleSelected);
        rightPanel = new VehicleSelectionPanel("Vehicle 2", controller, this::onVehicleSelected);
        resultView = new ComparisonResultView(calculator);
        GridPane directMpgPanel = createDirectMpgPanel();

        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));

        HBox vehicleSelection = new HBox(40);
        vehicleSelection.setMinHeight(500);
        vehicleSelection.setPadding(new Insets(10));
        vehicleSelection.getChildren().addAll(leftPanel, rightPanel);

        centerContent.getChildren().addAll(vehicleSelection, directMpgPanel, vehicleHabits);

        VBox resultSection = new VBox(40);
        resultSection.setPadding(new Insets(20));
        resultSection.getChildren().addAll(resultView);

        VBox mainContent = new VBox(20);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setMaxWidth(600);
        mainContent.setPadding(new Insets(20));

        mainContent.getChildren().addAll(
                header,
                centerContent,
                resultSection
        );

        this.setCenter(mainContent);
    }

    private VBox createInstructionHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(10));
        header.getStyleClass().add("instruction-header");

        Label title = new Label("Vehicle Fuel Economy Comparison Tool");
        title.getStyleClass().add("header-title");

        Text instructions = new Text(
                """
                        Learn how efficient fuel economy can save you money!
                        1. Select vehicles using the dropdown menus OR enter MPG values directly
                        2. Adjust fuel prices and driving habits as needed
                        3. Click 'Compare Vehicles' to see cost comparisons
                        4. Use 'Recalculate' to update calculations with new values"""
        );
        instructions.setWrappingWidth(600);

        header.getChildren().addAll(title, instructions);
        return header;

    }
    private Accordion createVehicleHabits() {
        Accordion accordion = new Accordion();
        accordion.getPanes().addAll(
                createFuelPricesPane(),
                financialSettingsPane
        );
        return accordion;
    }
    private TitledPane createFuelPricesPane() {
        TitledPane fuelPricesPane = new TitledPane();
        fuelPricesPane.setText("Fuel Prices and Driving Habits");

        VBox content = new VBox(20);
        content.setPadding(new Insets(10));
        content.getStyleClass().add("top-section");

        Label gasPriceLabel = new Label("Gas Price ($):");
        gasPriceField = new TextField(String.valueOf(3.50));
        gasPriceField.setMaxWidth(120);

        Label milesLabel = new Label("Annual Miles:");
        milesField = new TextField(String.valueOf(15000));
        milesField.setMaxWidth(120);

        Label timeLabel = new Label("Ownership Years:");
        timeField = new TextField(String.valueOf(5));
        timeField.setMaxWidth(120);

        Label electricityLabel = new Label("Electricity ($/kwh):");
        electricField = new TextField(String.valueOf(0.13));
        electricField.setMaxWidth(120);

        Button recalculateButton = new Button("Update Calculations");
        recalculateButton.setOnAction(_ -> recalculateCheck());

        content.getChildren().addAll(
                gasPriceLabel, gasPriceField,
                milesLabel, milesField,
                timeLabel, timeField,
                electricityLabel, electricField,
                recalculateButton
        );

        fuelPricesPane.setContent(content);
        return fuelPricesPane;
    }


    private GridPane createDirectMpgPanel() {
        GridPane panel = new GridPane();
        panel.setHgap(10);
        panel.setVgap(10);
        panel.setPadding(new Insets(10));
        panel.getStyleClass().add("mpg-input-panel");

        Label directInputLabel = new Label("Or directly compare MPG values: (Year will default to 2023)");
        directInputLabel.setStyle("-fx-font-weight: bold;");

        panel.add(directInputLabel, 0, 0, 2, 1);

        Label vehicle1Label = new Label("Vehicle 1 MPG:");
        mpg1Field = new TextField();
        mpg1Field.setPromptText("Enter MPG");
        mpg1Field.setPrefWidth(100);
        mpg1Field.setOnAction(_ -> compareVehicles());
        mpg1Field.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                compareVehicles();
            }
        });

        Label vehicle2Label = new Label("Vehicle 2 MPG:");
        mpg2Field = new TextField();
        mpg2Field.setPromptText("Enter MPG");
        mpg2Field.setPrefWidth(100);
        mpg2Field.setOnAction(_ -> compareVehicles());
        mpg2Field.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                compareVehicles();

            }
        });

        panel.add(vehicle1Label, 0, 1);
        panel.add(mpg1Field, 1, 1);
        panel.add(vehicle2Label, 0, 2);
        panel.add(mpg2Field, 1, 2);


        return panel;
    }
    private void onVehicleSelected() {
        if (leftPanel.getSelectedVehicle() != null && rightPanel.getSelectedVehicle() != null) {
            vehicle1 = leftPanel.getSelectedVehicle();
            vehicle2 = rightPanel.getSelectedVehicle();

            if (vehicle1 != null) {
                vehicle1.setPurchasePrice(vehicle1.getPurchasePrice());
                vehicle1.setDownPayment(vehicle1.getDownPayment());
                vehicle1.setInterestRate(vehicle1.getInterestRate());
                vehicle1.setLoanPeriod(vehicle1.getLoanPeriod());
            }

            if (vehicle2 != null) {
                vehicle2.setPurchasePrice(vehicle2.getPurchasePrice());
                vehicle2.setDownPayment(vehicle2.getDownPayment());
                vehicle2.setInterestRate(vehicle2.getInterestRate());
                vehicle2.setLoanPeriod(vehicle2.getLoanPeriod());
            }

            ((FuelComparisonControllerImpl)controller).setCurrentVehicles(vehicle1, vehicle2);
            updateResults();
        }
    }

    private void compareVehicles() {
        try {
            double purchasePrice1 = vehicle1 != null ? vehicle1.getPurchasePrice() : 10000;
            double downPayment1 = vehicle1 != null ? vehicle1.getDownPayment() : 0;
            double interestRate1 = vehicle1 != null ? vehicle1.getInterestRate() : 0;
            double loanPeriod1 = vehicle1 != null ? vehicle1.getLoanPeriod() : 0;

            double purchasePrice2 = vehicle2 != null ? vehicle2.getPurchasePrice() : 10000;
            double downPayment2 = vehicle2 != null ? vehicle2.getDownPayment() : 0;
            double interestRate2 = vehicle2 != null ? vehicle2.getInterestRate() : 0;
            double loanPeriod2 = vehicle2 != null ? vehicle2.getLoanPeriod() : 0;

            vehicle1 = controller.createVehicleFromInputs(mpg1Field.getText().trim(), "1", leftPanel.getSelectedVehicle());
            vehicle2 = controller.createVehicleFromInputs(mpg2Field.getText().trim(), "2", rightPanel.getSelectedVehicle());

            vehicle1.setPurchasePrice(purchasePrice1);
            vehicle1.setDownPayment(downPayment1);
            vehicle1.setInterestRate(interestRate1);
            vehicle1.setLoanPeriod(loanPeriod1);
            vehicle1.setLoanAmount(purchasePrice1 - downPayment1);

            vehicle2.setPurchasePrice(purchasePrice2);
            vehicle2.setDownPayment(downPayment2);
            vehicle2.setInterestRate(interestRate2);
            vehicle2.setLoanPeriod(loanPeriod2);
            vehicle2.setLoanAmount(purchasePrice2 - downPayment2);

            ((FuelComparisonControllerImpl)controller).setCurrentVehicles(vehicle1, vehicle2);

            resultView.showSuccess("");
            updateResults();
        } catch (IllegalArgumentException e) {
            resultView.showError(e.getMessage());
        }
    }


    private void updateResults() {
        ComparisonResult result = controller.compareVehicles(vehicle1, vehicle2);
        resultView.updateResults(result);
    }

    private void recalculateFinancials() {
        try {
            Object[] vehicle1Details = financialSettingsPane.getVehicleFinancialDetails(1);
            double purchasePrice1 = (double) vehicle1Details[0];
            double downPayment1 = (double) vehicle1Details[1];
            double loanAmount1 = (double) vehicle1Details[2];
            double interestRate1 = (double) vehicle1Details[3];
            double loanPeriod1 = (double) vehicle1Details[4];

            Object[] vehicle2Details = financialSettingsPane.getVehicleFinancialDetails(2);
            double purchasePrice2 = (double) vehicle2Details[0];
            double downPayment2 = (double) vehicle2Details[1];
            double loanAmount2 = (double) vehicle2Details[2];
            double interestRate2 = (double) vehicle2Details[3];
            double loanPeriod2 = (double) vehicle2Details[4];

            if (vehicle1 != null) {
                vehicle1.setPurchasePrice(purchasePrice1);
                vehicle1.setDownPayment(downPayment1);
                vehicle1.setLoanAmount(loanAmount1);
                vehicle1.setInterestRate(interestRate1);
                vehicle1.setLoanPeriod(loanPeriod1);
            }

            if (vehicle2 != null) {
                vehicle2.setPurchasePrice(purchasePrice2);
                vehicle2.setDownPayment(downPayment2);
                vehicle2.setLoanAmount(loanAmount2);
                vehicle2.setInterestRate(interestRate2);
                vehicle2.setLoanPeriod(loanPeriod2);
            }

            controller.updateFinancialSettings(
                    purchasePrice1, downPayment1, loanAmount1, interestRate1, loanPeriod1,
                    purchasePrice2, downPayment2, loanAmount2, interestRate2, loanPeriod2
            );

            resultView.showSuccess("Financial values updated successfully!");

            if (vehicle1 != null && vehicle2 != null) {
                updateResults();
            }
        } catch (IllegalArgumentException e) {
            resultView.showError(e.getMessage());
        }
    }

    private void recalculateCheck() {
        try {
            double gasPrice = Double.parseDouble(gasPriceField.getText());
            int miles = Integer.parseInt(milesField.getText());
            int years = Integer.parseInt(timeField.getText());
            double electricPrice = Double.parseDouble(electricField.getText());

            calculator.updateAllParameters(gasPrice, miles, years, electricPrice);

            controller.updateSettingsFromStrings(
                    gasPriceField.getText(),
                    milesField.getText(),
                    timeField.getText(),
                    electricField.getText()
            );

            resultView.showSuccess("Values updated successfully!");
            if (vehicle1 != null && vehicle2 != null) {
                updateResults();
            }
        } catch (IllegalArgumentException e) {
            resultView.showError(e.getMessage());
        }
    }

}