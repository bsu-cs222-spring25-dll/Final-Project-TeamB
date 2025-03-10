package bsu.edu.cs.view;
import bsu.edu.cs.model.Vehicle;
import bsu.edu.cs.model.VehicleDatabase;
import javafx.geometry.Insets;
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

    private Label titleLabel;
    private Label annualCostsLabel;
    private Label savingsLabel;
    private Label fiveYearSavingsLabel;
    private Label efficientVehicleLabel;
    private BarChart<String, Number> costChart;

    public ComparisonResultView(){
        super(15);
        this.setPadding(new Insets(10));

        titleLabel = new Label("Comparison Results");
        titleLabel.setFont(Font.font("System",FontWeight.BOLD, 16));

        annualCostsLabel = new Label("Annual fuel costs will appear here");
        savingsLabel = new Label("");
        fiveYearSavingsLabel = new Label("");
        efficientVehicleLabel = new Label("");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Vehicle");
        yAxis.setLabel("Annual Fuel Cost ($)");

        costChart = new BarChart<>(xAxis,yAxis);
        costChart.setTitle("Annual Fuel Cost Comparison");
        costChart.setAnimated(false);
        costChart.setLegendVisible(false);

        this.getChildren().addAll(titleLabel,annualCostsLabel,savingsLabel,fiveYearSavingsLabel,efficientVehicleLabel,costChart);
    }

    public void updateResults(Vehicle vehicle1, Vehicle vehicle2,
                              double annualCost1, double annualCost2,
                              double annualSavings, double fiveYearSavings,
                              String moreEfficientVehicle) {
        annualCostsLabel.setText(String.format("Annual Fuel Costs: %s: $%.2f | %s: $%.2f",
                vehicle1.make + " " + vehicle1.model, annualCost1,
                vehicle2.make + " " + vehicle2.model, annualCost2));
        savingsLabel.setText(String.format("Annual Savings: %.2f", annualSavings));
        fiveYearSavingsLabel.setText(String.format("5-year Savings: $%.2f", fiveYearSavings));

        String efficientText = "More Efficient Vehicle: " + moreEfficientVehicle;
        efficientVehicleLabel.setText(efficientText);

        costChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(vehicle1.make + " " + vehicle1.model, annualCost1));
        series.getData().add(new XYChart.Data<>(vehicle2.make + " " + vehicle2.model, annualCost2));

        costChart.getData().add(series);
    }

    public void showError(String message){
        annualCostsLabel.setText(message);
        annualCostsLabel.setTextFill(Color.RED);

        savingsLabel.setText("");
        fiveYearSavingsLabel.setText("");
        costChart.getData().clear();
    }
}