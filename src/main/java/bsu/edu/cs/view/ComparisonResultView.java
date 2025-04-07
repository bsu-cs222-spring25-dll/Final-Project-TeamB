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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ComparisonResultView extends VBox {

    private final Label annualCostsLabel;
    private final Label yearCostLabel;
    private final Label savingsLabel;
    private final Label yearSavingsLabel;
    private final Label efficientVehicleLabel;
    private final BarChart<String, Number> costChart;

    public ComparisonResultView(){
        super(15);
        this.setPadding(new Insets(10));
        this.getStyleClass().add("results-view");

        Label titleLabel = new Label("Comparison Results");
        titleLabel.setFont(Font.font("System",FontWeight.BOLD, 16));

        annualCostsLabel = new Label("Annual fuel costs will appear here");
        yearCostLabel = new Label("");
        savingsLabel = new Label("");
        yearSavingsLabel = new Label("");
        efficientVehicleLabel = new Label("");

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

        yearCostLabel.getStyleClass().add("comparison-label");
        savingsLabel.getStyleClass().add("comparison-label");
        yearSavingsLabel.getStyleClass().add("comparison-label");
        efficientVehicleLabel.getStyleClass().add("comparison-label");
        costChart.getStyleClass().add("chart");

        this.getChildren().addAll(titleLabel,annualCostsLabel,yearCostLabel,savingsLabel,yearSavingsLabel,efficientVehicleLabel,costChart);

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

        savingsLabel.setText(String.format("Annual Savings: $%.2f", result.annualSavings()));
        yearSavingsLabel.setText(String.format("%d year Savings: $%.2f",
                result.yearsOwned(), result.yearsSavings()));
        efficientVehicleLabel.setText("More Efficient Vehicle: " +
                result.moreEfficientVehicle());

        updateChart(result);
    }

    private void updateChart(ComparisonResult result) {
        costChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        XYChart.Data<String, Number> data1 = new XYChart.Data<>(
                result.vehicle1().getMake() + " " + result.vehicle1().getModel() +
                        " $" + Math.round(result.annualCost1() * 100) / 100.0,
                result.annualCost1());
        series.getData().add(data1);

        XYChart.Data<String, Number> data2 = new XYChart.Data<>(
                result.vehicle2().getMake() + " " + result.vehicle2().getModel() +
                        " $" + Math.round(result.annualCost2() * 100) / 100.0,
                result.annualCost2());
        series.getData().add(data2);

        costChart.getData().add(series);

        String efficientVehicle = result.moreEfficientVehicle();

        String vehicle1Name = result.vehicle1().getMake() + " " + result.vehicle1().getModel();
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

        savingsLabel.setText("");
        yearCostLabel.setText("");
        yearSavingsLabel.setText("");
        costChart.getData().clear();
        efficientVehicleLabel.setText("");
    }
    public void showSuccess(String message){
        annualCostsLabel.setText(message);
        annualCostsLabel.setTextFill(Color.GREEN);

        savingsLabel.setText("");
        yearCostLabel.setText("");
        yearSavingsLabel.setText("");
        costChart.getData().clear();
        efficientVehicleLabel.setText("");
    }
}