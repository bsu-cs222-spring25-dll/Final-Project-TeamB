package bsu.edu.cs.view;

import bsu.edu.cs.model.FuelCalculator;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.beans.value.ChangeListener;

public class FinancialSettingPane extends TitledPane {
    private final TextField purchasePriceField1;
    private final TextField downPayment1;
    private final TextField loanAmount1;
    private final TextField interestRate1;
    private final TextField loanPeriod1;

    private final TextField purchasePriceField2;
    private final TextField downPayment2;
    private final TextField loanAmount2;
    private final TextField interestRate2;
    private final TextField loanPeriod2;

    private final Button recalculateButton;

    public FinancialSettingPane(FuelCalculator calculator) {
        this.setText("Purchase and Loan Details");
        this.setExpanded(false);

        HBox content = new HBox(10);
        content.setPadding(new javafx.geometry.Insets(10));
        content.getStyleClass().add("top-section");

        VBox vehicleOne = new VBox(10);
        vehicleOne.setPadding(new javafx.geometry.Insets(10));

        VBox vehicleTwo = new VBox(10);
        vehicleTwo.setPadding(new javafx.geometry.Insets(10));

        Label title1 = new Label("Vehicle 1");
        Label title2 = new Label("Vehicle 2");

        Label purchaseLabel1 = new Label("Purchase Price ($):");
        purchasePriceField1 = new TextField();
        purchasePriceField1.setPromptText(String.valueOf(10000));

        Label downPaymentLabel1 = new Label("Down Payment ($):");
        downPayment1 = new TextField();
        downPayment1.setPromptText(String.valueOf(5000));

        Label loanLabel1 = new Label("Loan Amount ($):");
        loanAmount1 = new TextField();
        loanAmount1.setPromptText(String.valueOf(5000.00));
        loanAmount1.setEditable(false);

        Label interestLabel1 = new Label("Interest Rate (%):");
        interestRate1 = new TextField();
        interestRate1.setPromptText(String.valueOf(7.5));

        Label loanPeriodLabel1 = new Label("Loan Period (months):");
        loanPeriod1 = new TextField();
        loanPeriod1.setPromptText(String.valueOf(60));

        Label purchaseLabel2 = new Label("Purchase Price ($):");
        purchasePriceField2 = new TextField();
        purchasePriceField2.setPromptText(String.valueOf(10000));

        Label downPaymentLabel2 = new Label("Down Payment ($):");
        downPayment2 = new TextField();
        downPayment2.setPromptText(String.valueOf(5000));

        Label loanLabel2 = new Label("Loan Amount ($):");
        loanAmount2 = new TextField();
        loanAmount2.setPromptText(String.valueOf(5000.00));
        loanAmount2.setEditable(false);

        Label interestLabel2 = new Label("Interest Rate (%):");
        interestRate2 = new TextField();
        interestRate2.setPromptText(String.valueOf(7.5));

        Label loanPeriodLabel2 = new Label("Loan Period (months):");
        loanPeriod2 = new TextField();
        loanPeriod2.setPromptText(String.valueOf(60));

        recalculateButton = new Button("Recalculate Financials");
        recalculateButton.getStyleClass().add("financial-recalculate-button");

        setupAutoCalculation(purchasePriceField1, downPayment1, loanAmount1, calculator);
        setupAutoCalculation(purchasePriceField2, downPayment2, loanAmount2, calculator);

        vehicleOne.getChildren().addAll(title1,
                purchaseLabel1, purchasePriceField1,
                downPaymentLabel1, downPayment1,
                loanLabel1, loanAmount1,
                interestLabel1, interestRate1,
                loanPeriodLabel1, loanPeriod1,
                recalculateButton);

        vehicleTwo.getChildren().addAll(title2,
                purchaseLabel2, purchasePriceField2,
                downPaymentLabel2, downPayment2,
                loanLabel2, loanAmount2,
                interestLabel2, interestRate2,
                loanPeriodLabel2, loanPeriod2);

        content.getChildren().addAll(vehicleOne, vehicleTwo);
        this.setContent(content);
    }

    private void setupAutoCalculation(TextField purchaseField, TextField downPaymentField,
                                      TextField loanField, FuelCalculator calculator) {
        ChangeListener<String> calculateLoanAmount = (_, _, _) ->
                calculator.updateLoanAmount(purchaseField, downPaymentField, loanField);

        purchaseField.textProperty().addListener(calculateLoanAmount);
        downPaymentField.textProperty().addListener(calculateLoanAmount);
    }

    private double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(value);
    }

    public String getPurchasePrice1() {
        return purchasePriceField1.getText();
    }

    public String getDownPayment1() {
        return downPayment1.getText();
    }

    public String getLoanAmount1() {
        return loanAmount1.getText();
    }

    public String getInterestRate1() {
        return interestRate1.getText();
    }

    public String getLoanPeriod1() {
        return loanPeriod1.getText();
    }

    public String getPurchasePrice2() {
        return purchasePriceField2.getText();
    }

    public String getDownPayment2() {
        return downPayment2.getText();
    }

    public String getLoanAmount2() {
        return loanAmount2.getText();
    }

    public String getInterestRate2() {
        return interestRate2.getText();
    }

    public String getLoanPeriod2() {
        return loanPeriod2.getText();
    }

    public Button getRecalculateButton() {
        return recalculateButton;
    }

    public Object[] getVehicleFinancialDetails(int vehicleNumber) {
        if (vehicleNumber == 1) {
            return new Object[] {
                    parseDouble(getPurchasePrice1()),
                    parseDouble(getDownPayment1()),
                    parseDouble(getLoanAmount1()),
                    parseDouble(getInterestRate1()),
                    parseDouble(getLoanPeriod1())
            };
        } else if (vehicleNumber == 2) {
            return new Object[] {
                    parseDouble(getPurchasePrice2()),
                    parseDouble(getDownPayment2()),
                    parseDouble(getLoanAmount2()),
                    parseDouble(getInterestRate2()),
                    parseDouble(getLoanPeriod2())
            };
        } else {
            throw new IllegalArgumentException("Vehicle number must be 1 or 2");
        }
    }
}