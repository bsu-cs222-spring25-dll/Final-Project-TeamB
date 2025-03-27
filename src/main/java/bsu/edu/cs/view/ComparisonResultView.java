package bsu.edu.cs.view;
import bsu.edu.cs.model.Vehicle;
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

        this.getChildren().addAll(titleLabel,annualCostsLabel,yearCostLabel,savingsLabel, yearSavingsLabel,efficientVehicleLabel,costChart);

    }

    public void updateResults(Vehicle vehicle1, Vehicle vehicle2,
                              double annualCost1, double annualCost2, double yearCost1, double yearCost2,
                              double annualSavings, int yearsOwned, double yearSavings,
                              String moreEfficientVehicle) {
        annualCostsLabel.getStyleClass().add("comparison-label");

        annualCostsLabel.setText(String.format("Annual Fuel Costs: %s: $%.2f | %s: $%.2f",
                vehicle1.getMake() + " " + vehicle1.getModel(), annualCost1,
                vehicle2.getMake() + " " + vehicle2.getModel(), annualCost2));
        yearCostLabel.setText(String.format(yearsOwned + " Year Fuel Costs: %s: $%.2f | %s: $%.2f",
                vehicle1.getMake() + " " + vehicle1.getModel(), yearCost1,
                vehicle2.getMake() + " " + vehicle2.getModel(), yearCost2));
        savingsLabel.setText(String.format("Annual Savings: $%.2f", annualSavings));
        yearSavingsLabel.setText(String.format(yearsOwned + " year Savings: $%.2f", yearSavings));

        String efficientText = "More Efficient Vehicle: " + moreEfficientVehicle;
        efficientVehicleLabel.setText(efficientText);

        costChart.getData().clear();

        double roundedAnnualCost1 = Math.round(annualCost1 * 5) / 5.00;
        double roundedAnnualCost2 = Math.round(annualCost2 * 5) / 5.00;

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(vehicle1.getMake() + " " + vehicle1.getModel() + " $" + roundedAnnualCost1 + " ", annualCost1));

        series.getData().add(new XYChart.Data<>(vehicle2.getMake() + " " + vehicle2.getModel() + " $" + roundedAnnualCost2, annualCost2));

        costChart.getData().add(series);
        Node node = series.getData().getFirst().getNode();
        node.getStyleClass().add("chart-bar");
        if (moreEfficientVehicle.contains(vehicle1.getMake() + " " + vehicle1.getModel() + " ")) {
            node.getStyleClass().add("efficient");
        }

        Node node2 = series.getData().get(1).getNode();
        node2.getStyleClass().add("chart-bar");
        if (moreEfficientVehicle.contains(vehicle2.getMake() + " " + vehicle2.getModel())) {
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