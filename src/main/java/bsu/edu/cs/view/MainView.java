package bsu.edu.cs.view;

import bsu.edu.cs.controller.FuelComparisonController;
import bsu.edu.cs.model.ComparisonResult;
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

    public MainView(FuelComparisonController controller) throws Exception {
        this.controller = controller;

        String csvFilePath = "src/main/resources/vehicles.csv";

        VBox header = createInstructionHeader();
        Accordion vehicleHabits = createVehicleHabits();
        leftPanel = new VehicleSelectionPanel("Vehicle 1", csvFilePath);
        rightPanel = new VehicleSelectionPanel("Vehicle 2", csvFilePath);
        resultView = new ComparisonResultView();
        GridPane directMpgPanel = createDirectMpgPanel();

        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));

        HBox vehicleSelection = new HBox(40);
        vehicleSelection.setMinHeight(500);
        vehicleSelection.setPadding(new Insets(10));
        vehicleSelection.getChildren().addAll(leftPanel, rightPanel);

        centerContent.getChildren().addAll(
                vehicleSelection,
                directMpgPanel,
                vehicleHabits
        );

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
                        Learn how good fuel economy can save you money!
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

        TitledPane fuelPrices = new TitledPane();
        fuelPrices.setText("Fuel Prices and Driving Habits");

        VBox vehicleHabits = new VBox(20);
        vehicleHabits.setPadding(new Insets(10));
        vehicleHabits.getStyleClass().add("top-section");

        Label gasPriceLabel = new Label("Gas Price ($):");
        gasPriceField = new TextField(String.valueOf(3.50));
        gasPriceField.setMaxWidth(80);

        Label milesLabel = new Label("Annual Miles:");
        milesField = new TextField(String.valueOf(15000));
        milesField.setMaxWidth(80);

        Label timeLabel = new Label("Estimated Length of Ownership: ");
        timeField = new TextField(String.valueOf(5));
        timeField.setMaxWidth(80);

        Label electricityLabel = new Label("Electricity: ($/kwh)");
        electricField = new TextField(String.valueOf(0.13));
        electricField.setMaxWidth(80);

        Button recalculateButton = new Button("Recalculate");
        recalculateButton.setOnAction(_ -> recalculateCheck());

        vehicleHabits.getChildren().addAll(
                gasPriceLabel, gasPriceField,
                milesLabel, milesField,
                timeLabel, timeField,
                electricityLabel, electricField,
                recalculateButton);
        fuelPrices.setContent(vehicleHabits);
        accordion.getPanes().addAll(fuelPrices);
        return accordion;
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

        Label vehicle2Label = new Label("Vehicle 2 MPG:");
        mpg2Field = new TextField();
        mpg2Field.setPromptText("Enter MPG");
        mpg2Field.setPrefWidth(100);

        panel.add(vehicle1Label, 0, 1);
        panel.add(mpg1Field, 1, 1);
        panel.add(vehicle2Label, 0, 2);
        panel.add(mpg2Field, 1, 2);
        Button calculateButton = new Button("Compare Vehicles");
        calculateButton.setOnAction(_ -> compareVehicles());
        panel.add(calculateButton,0,4);

        return panel;
    }

    private void compareVehicles() {
        try {
            vehicle1 = controller.createVehicleFromInputs(mpg1Field.getText().trim(), "1", leftPanel.getSelectedVehicle());
            vehicle2 = controller.createVehicleFromInputs(mpg2Field.getText().trim(), "2", rightPanel.getSelectedVehicle());
            updateResults();
        } catch (IllegalArgumentException e) {
            resultView.showError(e.getMessage());
        }
    }

    private void updateResults() {
        ComparisonResult result = controller.compareVehicles(vehicle1, vehicle2);
        resultView.updateResults(result);
    }

    private void recalculateCheck() {
        try {
            controller.updateSettingsFromStrings(
                    gasPriceField.getText(),
                    milesField.getText(),
                    timeField.getText(),
                    electricField.getText()
            );
            resultView.showSuccess("Values updated successfully!");
        } catch (IllegalArgumentException e) {
            resultView.showError(e.getMessage());
        }
    }
}