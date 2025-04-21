package bsu.edu.cs.view;

import bsu.edu.cs.model.ComparisonResult;
import bsu.edu.cs.model.FuelCalculator;
import bsu.edu.cs.model.Vehicle;
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
import java.time.Year;
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

        addValueCell(1, 1, String.format("$%.2f", result.perMileCost1()), result);
        addValueCell(1, 2, String.format("$%.2f", result.perMileCost2()), result);

        addValueCell(2, 1, String.format("$%.2f", result.dayCost1()), result);
        addValueCell(2, 2, String.format("$%.2f", result.dayCost2()), result);

        addValueCell(3, 1, String.format("$%.2f", result.weekCost1()), result);
        addValueCell(3, 2, String.format("$%.2f", result.weekCost2()), result);

        addValueCell(4, 1, String.format("$%.2f", result.monthCost1()), result);
        addValueCell(4, 2, String.format("$%.2f", result.monthCost2()), result);

        addValueCell(5, 1, String.format("$%.2f", result.annualCost1()), result);
        addValueCell(5, 2, String.format("$%.2f", result.annualCost2()), result);

        addValueCell(6, 1, String.format("$%.2f", result.yearCost1()), result);
        addValueCell(6, 2, String.format("$%.2f", result.yearCost2()), result);

        addValueCell(7, 1, String.format("$%.2f", result.maintenanceCost1()), result);
        addValueCell(7, 2, String.format("$%.2f", result.maintenanceCost2()), result);

        addValueCell(8, 1, String.format("$%.2f", result.totalCost1()), result);
        addValueCell(8, 2, String.format("$%.2f", result.totalCost2()), result);

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

        try {
            double val1 = 0, val2 = 0;
            val2 = switch (row) {
                case 1 -> {
                    val1 = result.perMileCost1();
                    yield result.perMileCost2();
                }
                case 2 -> {
                    val1 = result.dayCost1();
                    yield result.dayCost2();
                }
                case 3 -> {
                    val1 = result.weekCost1();
                    yield result.weekCost2();
                }
                case 4 -> {
                    val1 = result.monthCost1();
                    yield result.monthCost2();
                }
                case 5 -> {
                    val1 = result.annualCost1();
                    yield result.annualCost2();
                }
                case 6 -> {
                    val1 = result.yearCost1();
                    yield result.yearCost2();
                }
                case 7 -> {
                    val1 = result.maintenanceCost1();
                    yield result.maintenanceCost2();
                }
                case 8 -> {
                    val1 = result.totalCost1();
                    yield result.totalCost2();
                }
                default -> val2;
            };
            tooltip.setText(getTooltipText(row, val1, val2, result));
        } catch (Exception e) {
            tooltip.setText(value);
        }

        Tooltip.install(valueLabel, tooltip);
        comparisonGrid.add(valueLabel, column, row);
    }

    private String getTooltipText(int row, double value1, double value2, ComparisonResult result) {
        double fuelPrice = calculator.getAnnualGasPrice();
        int annualMiles = calculator.getAnnualMiles();
        int ownershipYears = calculator.getYearsOwned();
        int currentYear = Year.now().getValue();

        Vehicle vehicle1 = result.vehicle1();
        Vehicle vehicle2 = result.vehicle2();

        return switch (row) {
            case 1 -> String.format("""
                            Fuel Cost per Mile Calculation:
                            
                            Vehicle 1: %s
                            $%.2f / %.1f MPG = $%.4f/mile
                            
                            Vehicle 2: %s
                            $%.2f / %.1f MPG = $%.4f/mile""",
                    vehicle1,
                    fuelPrice, vehicle1.getCombinedMpg(), value1,
                    vehicle2,
                    fuelPrice, vehicle2.getCombinedMpg(), value2);

            case 2 -> String.format("""
                            Daily Fuel Cost Calculation:
                            
                            Vehicle 1: ($%.2f annual / 365 days) = $%.2f/day
                            
                            Vehicle 2: ($%.2f annual / 365 days) = $%.2f/day""",
                    result.annualCost1(), value1,
                    result.annualCost2(), value2);

            case 3 -> String.format("""
                            Weekly Fuel Cost Calculation:
                            
                            Vehicle 1: ($%.2f daily * 7 days) = $%.2f/week
                            
                            Vehicle 2: ($%.2f daily * 7 days) = $%.2f/week""",
                    result.dayCost1(), value1,
                    result.dayCost2(), value2);

            case 4 -> String.format("""
                            Monthly Fuel Cost Calculation:
                            
                            Vehicle 1: ($%.2f annual / 12 months) = $%.2f/month
                            
                            Vehicle 2: ($%.2f annual / 12 months) = $%.2f/month""",
                    result.annualCost1(), value1,
                    result.annualCost2(), value2);

            case 5 -> String.format("""
                            Annual Fuel Cost Calculation:
                            
                            Vehicle 1: %d miles / %.1f MPG * $%.2f/gal = $%.2f
                            
                            Vehicle 2: %d miles / %.1f MPG * $%.2f/gal = $%.2f""",
                    annualMiles, vehicle1.getCombinedMpg(), fuelPrice, value1,
                    annualMiles, vehicle2.getCombinedMpg(), fuelPrice, value2);

            case 6 -> String.format("""
                            Ownership Period Fuel Cost:
                            
                            Vehicle 1: $%.2f annual * %d years = $%.2f
                            
                            Vehicle 2: $%.2f annual * %d years = $%.2f""",
                    result.annualCost1(), ownershipYears, value1,
                    result.annualCost2(), ownershipYears, value2);

            case 7 -> {
                double ageFactor1 = 1.0 + ((currentYear - vehicle1.getYear()) * 0.08);
                double ageFactor2 = 1.0 + ((currentYear - vehicle2.getYear()) * 0.08);
                yield String.format("""
                            Annual Maintenance Cost:
                            
                            Vehicle 1:
                            Base ($%.2f) + Mileage ($%.2f) + Age Factor(%.2f) = $%.2f
                            
                            Vehicle 2:
                            Base ($%.2f) + Mileage ($%.2f) + Age Factor(%.2f) = $%.2f""",
                    calculator.calculateYearlyMaintenance(vehicle1, annualMiles) -
                            (0.03 * annualMiles), ageFactor1,
                    (0.03 * annualMiles), value1,
                    calculator.calculateYearlyMaintenance(vehicle2, annualMiles) -
                            (0.03 * annualMiles), ageFactor2,
                    (0.03 * annualMiles), value2);
            }

            case 8 -> {
                double ageFactor1 = 1.0 + ((currentYear - vehicle1.getYear()) * 0.08);
                double ageFactor2 = 1.0 + ((currentYear - vehicle2.getYear()) * 0.08);

                MaintenanceCosts costs1 = getMaintenanceCosts(vehicle1);
                MaintenanceCosts costs2 = getMaintenanceCosts(vehicle2);

                yield String.format("""
                                Total Ownership Cost (%d years):
                                
                                Vehicle 1: %s
                                - Purchase: $%.2f
                                - Down Payment: $%.2f
                                - Loan: $%.2f @ %.2f%% for %.0f months
                                - Total Interest: $%.2f
                                - Fuel: $%.2f
                                - Maintenance: $%.2f
                                  (Base: $%.2f/yr, Mileage: $%.2f/yr, Age Factor: %.2fx)
                                --------------------------
                                TOTAL: $%.2f
                                
                                Vehicle 2: %s
                                - Purchase: $%.2f
                                - Down Payment: $%.2f
                                - Loan: $%.2f @ %.2f%% for %.0f months
                                - Total Interest: $%.2f
                                - Fuel: $%.2f
                                - Maintenance: $%.2f
                                  (Base: $%.2f/yr, Mileage: $%.2f/yr, Age Factor: %.2fx)
                                --------------------------
                                TOTAL: $%.2f""",
                        ownershipYears,
                        vehicle1,
                        vehicle1.getPurchasePrice(),
                        vehicle1.getDownPayment(),
                        vehicle1.getLoanAmount(),
                        vehicle1.getInterestRate(),
                        vehicle1.getLoanPeriod(),
                        vehicle1.calculateTotalInterest(),
                        result.yearCost1(),
                        result.maintenanceCost1() * ownershipYears,
                        costs1.baseCost,
                        costs1.mileageCost,
                        ageFactor1,
                        value1,
                        vehicle2,
                        vehicle2.getPurchasePrice(),
                        vehicle2.getDownPayment(),
                        vehicle2.getLoanAmount(),
                        vehicle2.getInterestRate(),
                        vehicle2.getLoanPeriod(),
                        vehicle2.calculateTotalInterest(),
                        result.yearCost2(),
                        result.maintenanceCost2() * ownershipYears,
                        costs2.baseCost,
                        costs2.mileageCost,
                        ageFactor2,
                        value2);
            }

            default -> String.format("""
                            Vehicle 1: $%.2f
                            Vehicle 2: $%.2f""",
                    value1, value2);
        };
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

    private record MaintenanceCosts(double baseCost, double mileageCost) {
    }

    private MaintenanceCosts getMaintenanceCosts(Vehicle vehicle) {
        double baseCost;
        double mileageFactor;

        if (vehicle.getFuelType() != null && vehicle.getFuelType().equals("Electricity")) {
            baseCost = 400;
            mileageFactor = 0.03;
        } else if (vehicle.getCombinedMpg() > 30) {
            baseCost = 550;
            mileageFactor = 0.045;
        } else {
            baseCost = 650;
            mileageFactor = 0.055;
        }

        return new MaintenanceCosts(
                baseCost,
                mileageFactor * calculator.getAnnualMiles()
        );
    }
}