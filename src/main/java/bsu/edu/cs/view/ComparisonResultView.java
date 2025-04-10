package bsu.edu.cs.view;

import bsu.edu.cs.model.ComparisonResult;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ComparisonResultView extends VBox {

    private final Label costPerMile;
    private final Label dailyCostLabel;
    private final Label weeklyCostLabel;
    private final Label monthlyCostLabel;
    private final Label annualCostsLabel;
    private final Label yearCostLabel;
    private final Label savingsLabel;
    private final Label yearSavingsLabel;
    private final Label efficientVehicleLabel;
    private final BarChart<String, Number> costChart;
    private final Label maintenanceLabel;
    private final Label totalCost;

    public ComparisonResultView(){
        super(15);
        this.setPadding(new Insets(10));
        this.getStyleClass().add("results-view");

        Label titleLabel = new Label("Comparison Results");
        titleLabel.getStyleClass().add("panel-title");

        costPerMile = new Label("");
        dailyCostLabel = new Label("");
        weeklyCostLabel = new Label("");
        monthlyCostLabel = new Label("");
        annualCostsLabel = new Label("Annual fuel costs will appear here");
        yearCostLabel = new Label("");
        savingsLabel = new Label("");
        yearSavingsLabel = new Label("");
        maintenanceLabel = new Label("");
        efficientVehicleLabel = new Label("");
        totalCost = new Label("");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Vehicle");
        yAxis.setLabel("Annual Fuel Cost ($)");

        costChart = new BarChart<>(xAxis,yAxis);
        costChart.setTitle("Annual Fuel Cost Comparison");
        costChart.setAnimated(false);
        costChart.setLegendVisible(false);
        costChart.setBarGap(0);
        costChart.setCategoryGap(100);
        costChart.setMinHeight(300);

        costPerMile.getStyleClass().add("comparison-label");
        dailyCostLabel.getStyleClass().add("comparison-label");
        weeklyCostLabel.getStyleClass().add("comparison-label");
        monthlyCostLabel.getStyleClass().add("comparison-label");
        yearCostLabel.getStyleClass().add("comparison-label");
        savingsLabel.getStyleClass().add("comparison-label");
        savingsLabel.setStyle("-fx-font-weight: bold;");
        yearSavingsLabel.getStyleClass().add("comparison-label");
        yearSavingsLabel.setStyle("-fx-font-weight: bold;");
        efficientVehicleLabel.getStyleClass().add("comparison-label");
        efficientVehicleLabel.setStyle("-fx-font-weight: bold;");
        maintenanceLabel.getStyleClass().add("comparison-label");
        maintenanceLabel.setStyle("-fx-font-weight: bold;");
        totalCost.getStyleClass().add("comparison-label");
        totalCost.setStyle("-fx-font-weight: bold;");
        costChart.getStyleClass().add("chart");

        this.getChildren().addAll(titleLabel,annualCostsLabel,yearCostLabel,savingsLabel,
                yearSavingsLabel,maintenanceLabel,totalCost,monthlyCostLabel,weeklyCostLabel,dailyCostLabel,
                costPerMile,efficientVehicleLabel,costChart);

    }

    public void updateResults(ComparisonResult result) {

        annualCostsLabel.getStyleClass().add("comparison-label");
        annualCostsLabel.setText(String.format("Annual Fuel Costs: %s: $%.2f | %s: $%.2f",
                result.vehicle1().getMake() + " " + result.vehicle1().getModel(),
                result.annualCost1(),
                result.vehicle2().getMake() + " " + result.vehicle2().getModel(),
                result.annualCost2()));
        yearCostLabel.setText(String.format("%d Year Fuel Costs: %s: $%.2f | %s: $%.2f",
                result.yearsOwned(),
                result.vehicle1().getMake() + " " + result.vehicle1().getModel(),
                result.yearCost1(),
                result.vehicle2().getMake() + " " + result.vehicle2().getModel(),
                result.yearCost2()));
        monthlyCostLabel.setText(String.format("Monthly Fuel Cost: %s: $%.2f | %s: $%.2f",
                result.vehicle1().getMake() + " " + result.vehicle1().getModel(),
                result.monthCost1(),
                result.vehicle2().getMake() + " " + result.vehicle2().getModel(),
                result.monthCost2()));
        weeklyCostLabel.setText(String.format("Weekly Fuel Cost: %s: $%.2f | %s: $%.2f",
                result.vehicle1().getMake() + " " + result.vehicle1().getModel(),
                result.weekCost1(),
                result.vehicle2().getMake() + " " + result.vehicle2().getModel(),
                result.weekCost2()));
        dailyCostLabel.setText(String.format("Daily Fuel Cost: %s: $%.2f | %s: $%.2f",
                result.vehicle1().getMake() + " " + result.vehicle1().getModel(),
                result.dayCost1(),
                result.vehicle2().getMake() + " " + result.vehicle2().getModel(),
                result.dayCost2()));
        costPerMile.setText(String.format("Fuel Cost per Mile: %s: $%.2f | %s: $%.2f",
                result.vehicle1().getMake() + " " + result.vehicle1().getModel(),
                result.perMileCost1(),
                result.vehicle2().getMake() + " " + result.vehicle2().getModel(),
                result.perMileCost2()));

        savingsLabel.setText(String.format("Annual Savings: $%.2f", result.annualSavings()));
        yearSavingsLabel.setText(String.format("%d year Savings: $%.2f",
                result.yearsOwned(), result.yearsSavings()));
        maintenanceLabel.setText(String.format(
                "Annual Maintenance: %s: $%.2f | %s: $%.2f",
                result.vehicle1().getMake() + " " + result.vehicle1().getModel(),
                result.maintenanceCost1(),
                result.vehicle2().getMake() + " " + result.vehicle2().getModel(),
                result.maintenanceCost2()
        ));
        totalCost.setText(String.format(
                "Total Cost of %s: $%.2f | %s: $%.2f",
                result.vehicle1().getMake() + " " + result.vehicle1().getModel(),
                result.totalCost1(),
                result.vehicle2().getMake() + " " + result.vehicle2().getModel(),
                result.totalCost2()
        ));
        efficientVehicleLabel.setText("More Efficient Vehicle: " +
                result.moreEfficientVehicle());

        updateChart(result);
    }

    private void updateChart(ComparisonResult result) {
        costChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        XYChart.Data<String, Number> data1 = new XYChart.Data<>(
                result.vehicle1().getMake() + " " + result.vehicle1().getModel() +
                        " $" +  String.format("%.2f" ,result.annualCost1()),
                result.annualCost1());
        series.getData().add(data1);

        XYChart.Data<String, Number> data2 = new XYChart.Data<>(
                result.vehicle2().getMake() + " " + result.vehicle2().getModel() +
                        " $" + String.format("%.2f" ,result.annualCost1()),
                result.annualCost2());
        series.getData().add(data2);

        costChart.getData().add(series);

        String efficientVehicle = result.moreEfficientVehicle();

        String vehicle1Name = result.vehicle1().getMake() + " " + result.vehicle1().getModel() + " ";
        Node node1 = data1.getNode();
        node1.getStyleClass().add("chart-bar");
        if (efficientVehicle.contains(vehicle1Name)) {
            node1.getStyleClass().add("efficient");
        }

        String vehicle2Name = result.vehicle2().getMake() + " " + result.vehicle2().getModel();
        Node node2 = data2.getNode();
        node2.getStyleClass().add("chart-bar");
        if (efficientVehicle.contains(vehicle2Name)) {
            node2.getStyleClass().add("efficient");
        }
    }

    public void showError(String message){
        annualCostsLabel.setText(message);
        annualCostsLabel.setTextFill(Color.RED);

        costPerMile.setText("");
        dailyCostLabel.setText("");
        weeklyCostLabel.setText("");
        monthlyCostLabel.setText("");
        savingsLabel.setText("");
        yearCostLabel.setText("");
        yearSavingsLabel.setText("");
        maintenanceLabel.setText("");
        totalCost.setText("");
        costChart.getData().clear();
        efficientVehicleLabel.setText("");
    }
    public void showSuccess(String message){
        annualCostsLabel.setText(message);
        annualCostsLabel.setTextFill(Color.GREEN);

        costPerMile.setText("");
        dailyCostLabel.setText("");
        weeklyCostLabel.setText("");
        monthlyCostLabel.setText("");
        savingsLabel.setText("");
        yearCostLabel.setText("");
        yearSavingsLabel.setText("");
        maintenanceLabel.setText("");
        totalCost.setText("");
        costChart.getData().clear();
        efficientVehicleLabel.setText("");
    }
}