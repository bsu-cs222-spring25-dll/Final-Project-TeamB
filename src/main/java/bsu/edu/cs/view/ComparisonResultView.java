package bsu.edu.cs.view;

import bsu.edu.cs.model.ComparisonResult;
import bsu.edu.cs.model.FuelCalculator;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class ComparisonResultView extends VBox {

    private final BarChart<String, Number> costChart;
    private final GridPane comparisonGrid;
    private final Label vehicle1HeaderLabel;
    private final Label vehicle2HeaderLabel;
    private final Label errorMessageLabel;
    private final Label efficientVehicleLabel;
    private final FuelCalculator calculator;

    public ComparisonResultView(FuelCalculator calculator) {
        super(15);
        this.calculator = calculator;
        this.setPadding(new Insets(10));
        this.getStyleClass().add("results-view");

        Label titleLabel = new Label("Comparison Results");
        titleLabel.getStyleClass().add("panel-title");

        errorMessageLabel = new Label("Annual fuel costs will appear here");
        errorMessageLabel.getStyleClass().add("comparison-label");

        efficientVehicleLabel = new Label("");
        efficientVehicleLabel.getStyleClass().add("comparison-label");
        efficientVehicleLabel.setStyle("-fx-font-weight: bold;");

        comparisonGrid = new GridPane();
        comparisonGrid.setHgap(20);
        comparisonGrid.setVgap(10);
        comparisonGrid.setPadding(new Insets(10));
        comparisonGrid.getStyleClass().add("comparison-grid");

        vehicle1HeaderLabel = new Label();
        vehicle2HeaderLabel = new Label();

        vehicle1HeaderLabel.getStyleClass().add("vehicle-header");
        vehicle2HeaderLabel.getStyleClass().add("vehicle-header");

        addMetricRow(0, "");
        addMetricRow(1, "Fuel Cost per Mile");
        addMetricRow(2, "Daily Fuel Cost");
        addMetricRow(3, "Weekly Fuel Cost");
        addMetricRow(4, "Monthly Fuel Cost");
        addMetricRow(5, "Annual Fuel Cost");
        addMetricRow(6, "Fuel Cost over Ownership");
        addMetricRow(7, "Annual Maintenance");
        addMetricRow(8, "Total Cost over Ownership");
        addMetricRow(9, "Annual Savings");
        addMetricRow(10, "Total Years Savings");

        comparisonGrid.add(vehicle1HeaderLabel, 1, 0);
        comparisonGrid.add(vehicle2HeaderLabel, 2, 0);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Vehicle");
        yAxis.setLabel("Annual Fuel Cost ($)");

        costChart = new BarChart<>(xAxis, yAxis);
        costChart.setTitle("Annual Fuel Cost Comparison");
        costChart.setAnimated(false);
        costChart.setLegendVisible(false);
        costChart.setBarGap(0);
        costChart.setCategoryGap(100);
        costChart.setMinHeight(300);
        costChart.getStyleClass().add("chart");

        this.getChildren().addAll(
                titleLabel,
                errorMessageLabel,
                efficientVehicleLabel,
                comparisonGrid,
                costChart
        );
    }

    private void addMetricRow(int rowIndex, String metricName) {
        Label metricLabel = new Label(metricName);
        metricLabel.getStyleClass().add("metric-label");
        if (rowIndex > 0) {
            metricLabel.setStyle("-fx-font-weight: bold;");
        }
        comparisonGrid.add(metricLabel, 0, rowIndex);
    }

    public void updateResults(ComparisonResult result) {
        errorMessageLabel.setTextFill(Color.BLACK);
        clearComparisonGrid();

        double fuelPrice = calculator.getAnnualGasPrice();
        int annualMiles = calculator.getAnnualMiles();
        int ownershipYears = calculator.getYearsOwned();
        double electricityPrice = calculator.getElectricityPricePerKWH();

        String vehicle1Name = result.vehicle1().getYear() + " " + result.vehicle1().getMake() + " " + result.vehicle1().getModel();
        String vehicle2Name = result.vehicle2().getYear() + " " + result.vehicle2().getMake() + " " + result.vehicle2().getModel();
        vehicle1HeaderLabel.setText(vehicle1Name);
        vehicle2HeaderLabel.setText(vehicle2Name);

        Label paramsLabel = new Label(String.format(
                "Current Parameters: %d miles/yr | %d years | $%.2f/gal | $%.2f/kWh",
                annualMiles,
                ownershipYears,
                fuelPrice,
                electricityPrice
        ));
        paramsLabel.getStyleClass().add("parameters-label");
        comparisonGrid.add(paramsLabel, 0, 11, 3, 1);

        ComparisonResult.CostBreakdown cost1 = result.cost1();
        ComparisonResult.CostBreakdown cost2 = result.cost2();

        addValueCell(1, 1, String.format("$%.2f", cost1.perMile()), result);
        addValueCell(1, 2, String.format("$%.2f", cost2.perMile()), result);

        addValueCell(2, 1, String.format("$%.2f", cost1.daily()), result);
        addValueCell(2, 2, String.format("$%.2f", cost2.daily()), result);

        addValueCell(3, 1, String.format("$%.2f", cost1.weekly()), result);
        addValueCell(3, 2, String.format("$%.2f", cost2.weekly()), result);

        addValueCell(4, 1, String.format("$%.2f", cost1.monthly()), result);
        addValueCell(4, 2, String.format("$%.2f", cost2.monthly()), result);

        addValueCell(5, 1, String.format("$%.2f", cost1.annual()), result);
        addValueCell(5, 2, String.format("$%.2f", cost2.annual()), result);

        addValueCell(6, 1, String.format("$%.2f", cost1.annual() * ownershipYears), result);
        addValueCell(6, 2, String.format("$%.2f", cost2.annual() * ownershipYears), result);

        addValueCell(7, 1, String.format("$%.2f", cost1.maintenance()), result);
        addValueCell(7, 2, String.format("$%.2f", cost2.maintenance()), result);

        addValueCell(8, 1, String.format("$%.2f", cost1.total()), result);
        addValueCell(8, 2, String.format("$%.2f", cost2.total()), result);

        Label savingsLabel = new Label(String.format("$%.2f", result.annualSavings()));
        savingsLabel.getStyleClass().add("comparison-value");
        savingsLabel.setStyle("-fx-font-weight: bold;");
        comparisonGrid.add(savingsLabel, 1, 9, 2, 1);

        Label yearSavingsLabel = new Label(String.format("$%.2f", result.yearsSavings()));
        yearSavingsLabel.getStyleClass().add("comparison-value");
        yearSavingsLabel.setStyle("-fx-font-weight: bold;");
        comparisonGrid.add(yearSavingsLabel, 1, 10, 2, 1);

        efficientVehicleLabel.setText("More Efficient Vehicle: " + result.moreEfficientVehicle());
        highlightEfficientValues();

        updateChart(result);
    }

    private void addValueCell(int row, int column, String value, ComparisonResult result) {
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("comparison-value");

        Tooltip tooltip = new Tooltip();
        tooltip.setShowDelay(Duration.millis(100));
        tooltip.setHideDelay(Duration.millis(200));
        tooltip.setShowDuration(Duration.INDEFINITE);

        tooltip.setText(calculator.getTooltipText(row, result));
        Tooltip.install(valueLabel, tooltip);
        comparisonGrid.add(valueLabel, column, row);
    }

    private void highlightEfficientValues() {
        for (int row = 1; row <= 8; row++) {
            Label label1 = findLabelInGrid(1, row);
            Label label2 = findLabelInGrid(2, row);

            if (label1 != null && label2 != null) {
                try {
                    double val1 = Double.parseDouble(label1.getText().replace("$", ""));
                    double val2 = Double.parseDouble(label2.getText().replace("$", ""));

                    if (val1 < val2) {
                        label1.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    } else if (val2 < val1) {
                        label2.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    }
                } catch (NumberFormatException _) {
                }
            }
        }
    }

    private Label findLabelInGrid(int column, int row) {
        for (Node node : comparisonGrid.getChildren()) {
            if (GridPane.getColumnIndex(node) == column &&
                    GridPane.getRowIndex(node) == row &&
                    node instanceof Label) {
                return (Label) node;
            }
        }
        return null;
    }

    private void updateChart(ComparisonResult result) {
        costChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        String vehicle1Name = result.vehicle1().getMake() + " " + result.vehicle1().getModel();
        String vehicle2Name = result.vehicle2().getMake() + " " + result.vehicle2().getModel();

        ComparisonResult.CostBreakdown cost1 = result.cost1();
        ComparisonResult.CostBreakdown cost2 = result.cost2();

        XYChart.Data<String, Number> data1 = new XYChart.Data<>(
                vehicle1Name + " $" + String.format("%.2f", cost1.annual()),
                cost1.annual());
        series.getData().add(data1);

        XYChart.Data<String, Number> data2 = new XYChart.Data<>(
                vehicle2Name + " $" + String.format("%.2f", cost2.annual()),
                cost2.annual());
        series.getData().add(data2);

        costChart.getData().add(series);

        String efficientVehicle = result.moreEfficientVehicle();

        Node node1 = data1.getNode();
        node1.getStyleClass().add("chart-bar");
        if (efficientVehicle.contains(vehicle1Name)) {
            node1.getStyleClass().add("efficient");
        }

        Node node2 = data2.getNode();
        node2.getStyleClass().add("chart-bar");
        if (efficientVehicle.contains(vehicle2Name)) {
            node2.getStyleClass().add("efficient");
        }
    }

    public void showError(String message) {
        errorMessageLabel.setText(message);
        errorMessageLabel.setTextFill(Color.RED);
        clearComparisonGrid();
        costChart.getData().clear();
    }

    public void showSuccess(String message) {
        errorMessageLabel.setText(message);
        errorMessageLabel.setTextFill(Color.GREEN);
        clearComparisonGrid();
        costChart.getData().clear();
    }

    public void clearComparisonGrid() {
        List<Node> nodesToRemove = new ArrayList<>();

        for (Node node : comparisonGrid.getChildren()) {
            Integer columnIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            if ((columnIndex != null && columnIndex > 0 && rowIndex != null && rowIndex > 0) ||
                    (rowIndex != null && rowIndex == 11)) {
                nodesToRemove.add(node);
            }
        }

        comparisonGrid.getChildren().removeAll(nodesToRemove);

        vehicle1HeaderLabel.setText("Vehicle 1");
        vehicle2HeaderLabel.setText("Vehicle 2");
        efficientVehicleLabel.setText("");
    }
}