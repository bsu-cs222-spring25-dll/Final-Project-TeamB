package bsu.edu.cs.view;

import bsu.edu.cs.model.ComparisonResult;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ComparisonResultView extends VBox {

    private final BarChart<String, Number> costChart;
    private final GridPane comparisonGrid;
    private final Label vehicle1HeaderLabel;
    private final Label vehicle2HeaderLabel;
    private final Label errorMessageLabel;
    private final Label efficientVehicleLabel;

    public ComparisonResultView() {
        super(15);
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

        vehicle1HeaderLabel = new Label("Vehicle 1");
        vehicle2HeaderLabel = new Label("Vehicle 2");

        vehicle1HeaderLabel.getStyleClass().add("vehicle-header");
        vehicle2HeaderLabel.getStyleClass().add("vehicle-header");

        addMetricRow(0, "");  // Header row
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

        String vehicle1Name = result.vehicle1().getMake() + " " + result.vehicle1().getModel();
        String vehicle2Name = result.vehicle2().getMake() + " " + result.vehicle2().getModel();
        vehicle1HeaderLabel.setText(vehicle1Name);
        vehicle2HeaderLabel.setText(vehicle2Name);

        clearComparisonGrid();

        addValueCell(1, 1, String.format("$%.2f", result.perMileCost1()));
        addValueCell(1, 2, String.format("$%.2f", result.perMileCost2()));

        addValueCell(2, 1, String.format("$%.2f", result.dayCost1()));
        addValueCell(2, 2, String.format("$%.2f", result.dayCost2()));

        addValueCell(3, 1, String.format("$%.2f", result.weekCost1()));
        addValueCell(3, 2, String.format("$%.2f", result.weekCost2()));

        addValueCell(4, 1, String.format("$%.2f", result.monthCost1()));
        addValueCell(4, 2, String.format("$%.2f", result.monthCost2()));

        addValueCell(5, 1, String.format("$%.2f", result.annualCost1()));
        addValueCell(5, 2, String.format("$%.2f", result.annualCost2()));

        addValueCell(6, 1, String.format("$%.2f", result.yearCost1()));
        addValueCell(6, 2, String.format("$%.2f", result.yearCost2()));

        addValueCell(7, 1, String.format("$%.2f", result.maintenanceCost1()));
        addValueCell(7, 2, String.format("$%.2f", result.maintenanceCost2()));

        addValueCell(8, 1, String.format("$%.2f", result.totalCost1()));
        addValueCell(8, 2, String.format("$%.2f", result.totalCost2()));

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

    private void addValueCell(int row, int column, String value) {
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("comparison-value");
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

        XYChart.Data<String, Number> data1 = new XYChart.Data<>(
                vehicle1Name + " $" + String.format("%.2f", result.annualCost1()),
                result.annualCost1());
        series.getData().add(data1);

        XYChart.Data<String, Number> data2 = new XYChart.Data<>(
                vehicle2Name + " $" + String.format("%.2f", result.annualCost2()),
                result.annualCost2());
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
            if (columnIndex != null && columnIndex > 0 && rowIndex != null && rowIndex > 0) {
                nodesToRemove.add(node);
            }
        }

        comparisonGrid.getChildren().removeAll(nodesToRemove);

        vehicle1HeaderLabel.setText("Vehicle 1");
        vehicle2HeaderLabel.setText("Vehicle 2");
        efficientVehicleLabel.setText("");
    }
}