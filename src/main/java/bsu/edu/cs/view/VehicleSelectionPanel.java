package bsu.edu.cs.view;

import bsu.edu.cs.controller.FuelComparisonController;
import bsu.edu.cs.model.FuelCalculator;
import bsu.edu.cs.model.Vehicle;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class VehicleSelectionPanel extends VBox {
    private final ComboBox<String> yearCombo;
    private final ComboBox<String> makeCombo;
    private final ComboBox<String> modelCombo;
    private final ComboBox<String> trimCombo;
    private final TextArea resultArea;
    private final FuelComparisonController controller;
    private Vehicle selectedVehicle;
    private final Runnable onVehicleSelected;
    private final TextField mpgField;
    private final GridPane dropdownPane;
    private final FuelCalculator calculator;

    public VehicleSelectionPanel(String title, FuelComparisonController controller, Runnable onVehicleSelected, Runnable onMpgEntered, FuelCalculator calculator) {
        super(10);
        this.controller = controller;
        this.onVehicleSelected = onVehicleSelected;
        this.calculator = calculator;
        this.setPadding(new Insets(15));
        this.getStyleClass().add("vehicle-panel");

        HBox titleBar = new HBox(10);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("panel-title");

        Label mpgLabel = new Label("Mpg:");
        mpgField = new TextField();
        mpgField.setPromptText("Mpg");
        mpgField.setMaxWidth(70);

        resultArea = new TextArea();

        mpgField.textProperty().addListener((_, _, newValue) -> {
            boolean hasMpg = !newValue.trim().isEmpty();
            toggleDatabaseSectionVisibility(!hasMpg);

            if (hasMpg) {
                try {
                    Vehicle temp = controller.createVehicleFromInputs(newValue.trim(), "temp", null);
                    displayVehicleDetails(temp);
                    selectedVehicle = temp;

                    if (onMpgEntered != null) {
                        onMpgEntered.run();
                    }
                } catch (IllegalArgumentException e) {
                    resultArea.clear();
                }
            }
        });

        mpgField.setOnAction(_ -> {
            if (!mpgField.getText().trim().isEmpty() && onMpgEntered != null) {
                onMpgEntered.run();
            }
        });

        titleBar.getChildren().addAll(titleLabel, mpgLabel, mpgField);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        dropdownPane = new GridPane();
        dropdownPane.setHgap(10);
        dropdownPane.setVgap(10);

        yearCombo = createComboBox("Year:", 0, dropdownPane);
        yearCombo.setOnAction(_ -> fetchMakes());

        makeCombo = createComboBox("Make:", 1, dropdownPane);
        makeCombo.setDisable(true);
        makeCombo.setOnAction(_ -> fetchModels());

        modelCombo = createComboBox("Model:", 2, dropdownPane);
        modelCombo.setDisable(true);
        modelCombo.setOnAction(_ -> fetchTrims());

        trimCombo = createComboBox("Trim:", 3, dropdownPane);
        trimCombo.setDisable(true);
        trimCombo.setOnAction( _ -> searchVehicle());

        trimCombo.focusedProperty().addListener((_, _, newValue) -> {
                    if (!newValue) {
                        searchVehicle();
                    }
                });

        resultArea.setEditable(false);
        resultArea.setPrefWidth(150);
        resultArea.setWrapText(true);
        dropdownPane.add(resultArea, 0, 5, 2, 1);

        populateYears();

        this.getChildren().addAll(titleBar, dropdownPane);
    }

    private void toggleDatabaseSectionVisibility(boolean visible) {
        dropdownPane.setVisible(visible);
        dropdownPane.setManaged(visible);

        if (visible && mpgField.getText().trim().isEmpty()){
            resultArea.clear();
            selectedVehicle = null;
        }
    }

    private void populateYears() {
        controller.getYears()
                .thenAcceptAsync(years -> Platform.runLater(() -> {
                    yearCombo.getItems().clear();
                    yearCombo.getItems().addAll(years);
                }))
                .exceptionally(this::handleError);
    }

    private void fetchMakes() {
        String selectedYear = yearCombo.getValue();
        if (selectedYear == null) return;
        makeCombo.setDisable(false);

        controller.getMakes(selectedYear)
                .thenAcceptAsync(makes -> Platform.runLater(() -> {
                    makeCombo.getItems().clear();
                    makeCombo.getItems().addAll(makes);
                    modelCombo.getItems().clear();
                    modelCombo.setDisable(true);
                    trimCombo.getItems().clear();
                    trimCombo.setDisable(true);
                    resultArea.clear();
                }))
                .exceptionally(this::handleError);
    }

    private void fetchModels() {
        String selectedYear = yearCombo.getValue();
        String selectedMake = makeCombo.getValue();
        if (selectedYear == null || selectedMake == null) return;

        controller.getModels(selectedYear, selectedMake)
                .thenAcceptAsync(models -> Platform.runLater(() -> {
                    modelCombo.getItems().clear();
                    modelCombo.getItems().addAll(models);
                    modelCombo.setDisable(false);
                    trimCombo.getItems().clear();
                    trimCombo.setDisable(true);
                    resultArea.clear();
                }))
                .exceptionally(this::handleError);
    }

    private void fetchTrims() {
        String selectedYear = yearCombo.getValue();
        String selectedMake = makeCombo.getValue();
        String selectedModel = modelCombo.getValue();
        if (selectedYear == null || selectedMake == null || selectedModel == null) return;

        trimCombo.setDisable(true);
        resultArea.clear();

        controller.getTrims(selectedYear, selectedMake, selectedModel)
                .thenAcceptAsync(trims -> Platform.runLater(() -> {
                    trimCombo.getItems().clear();
                    if (trims == null || trims.isEmpty()) {
                        trimCombo.getItems().add("No Trim");
                        resultArea.setText("No trims available for this vehicle");
                    } else {
                        trimCombo.getItems().addAll(trims);
                    }
                    trimCombo.setDisable(false);
                    trimCombo.getSelectionModel().selectFirst();
                }))
                .exceptionally(this::handleError);
    }

    private void searchVehicle() {
        String selectedYear = yearCombo.getValue();
        String selectedMake = makeCombo.getValue();
        String selectedModel = modelCombo.getValue();
        String selectedTrim = trimCombo.getValue();

        trimCombo.setDisable(true);

        controller.searchVehicle(selectedYear, selectedMake, selectedModel, selectedTrim)
                .thenAcceptAsync(vehicle -> Platform.runLater(() -> {
                    if (vehicle != null) {
                        selectedVehicle = vehicle;
                        displayVehicleDetails(vehicle);


                        mpgField.clear();

                        if (onVehicleSelected != null) {
                            onVehicleSelected.run();
                        }
                    }
                    trimCombo.setDisable(false);
                }))
                .exceptionally(this::handleError);
    }

    private ComboBox<String> createComboBox(String labelText, int row, GridPane gridPane){
        Label label = new Label(labelText);
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(label,0,row);
        gridPane.add(comboBox,1,row);
        return comboBox;
    }

    private void displayVehicleDetails(Vehicle vehicle) {
        calculator.displayVehicleDetails(vehicle, resultArea);
    }

    private Void handleError(Throwable throwable) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred");
            alert.setContentText(throwable.getMessage());
            alert.showAndWait();
        });
        return null;
    }

    public Vehicle getSelectedVehicle() {
        return selectedVehicle;
    }

    public String getMpgValue() {
        return mpgField.getText().trim();
    }
}