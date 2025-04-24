package bsu.edu.cs.model;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.Year;

public class FuelCalculator {
    private double annualGasPrice;
    private int annualMiles;
    private int yearsOwned;
    private double electricityPricePerKWH;
    private final double kwhPerGallonEquivalent;


    public FuelCalculator() {
        this.annualGasPrice = 3.50;
        this.annualMiles = 15000;
        this.yearsOwned = 5;
        this.electricityPricePerKWH = 0.13;
        this.kwhPerGallonEquivalent = 33.705;
    }

    public void updateAllParameters(double annualGasPrice, int annualMiles, int yearsOwned, double electricityPricePerKWH) {
        setAnnualGasPrice(annualGasPrice);
        setAnnualMiles(annualMiles);
        setYearsOwned(yearsOwned);
        setElectricityPricePerKWH(electricityPricePerKWH);
    }

    public Double getAnnualGasPrice() {
        return annualGasPrice;
    }
    public void setAnnualGasPrice(double annualGasPrice) {
        if(annualGasPrice > 0){
            this.annualGasPrice = annualGasPrice;
        }
    }

    public int getAnnualMiles() {return annualMiles;}
    public void setAnnualMiles(int annualMiles) {
        if (annualMiles > 0){
            this.annualMiles = annualMiles;
        }
    }

    public int getYearsOwned() {return yearsOwned;}
    public void setYearsOwned(int yearsOwned) {
        if (yearsOwned > 0) {
            this.yearsOwned = yearsOwned;
        }
    }

    public void setElectricityPricePerKWH(double electricityPricePerKWH){
        if (electricityPricePerKWH > 0) {
            this.electricityPricePerKWH = electricityPricePerKWH;
        }
    }
    public double getElectricityPricePerKWH() {
        return electricityPricePerKWH;
    }


    public ComparisonResult compareVehicles(Vehicle vehicle1, Vehicle vehicle2) {
        ComparisonResult.CostBreakdown cost1 = calculateCostBreakdown(vehicle1);
        ComparisonResult.CostBreakdown cost2 = calculateCostBreakdown(vehicle2);

        double annualSavings = calculateOneYearSavings(vehicle1, vehicle2);
        double yearsSavings = calculateYearSavings(vehicle1, vehicle2);
        String moreEfficient = getMoreEfficientVehicle(vehicle1, vehicle2);
        return new ComparisonResult(
                vehicle1,
                vehicle2,
                cost1,
                cost2,
                annualSavings,
                yearsOwned,
                yearsSavings,
                moreEfficient
        );
    }

    private ComparisonResult.CostBreakdown calculateCostBreakdown(Vehicle vehicle) {
        double annual = calculateAnnualFuelCost(vehicle);
        double monthly = calculateMonthlyFuelCost(vehicle);
        double weekly = calculateWeeklyFuelCost(vehicle);
        double daily = calculateDailyFuelCost(vehicle);
        double perMile = calculateCostPerMile(vehicle);
        double maintenance = calculateYearlyMaintenance(vehicle, annualMiles);
        double total = vehicle.calculateTotalCostOfOwnership(annualMiles, yearsOwned, annualGasPrice, electricityPricePerKWH);

        return new ComparisonResult.CostBreakdown(annual, monthly, weekly, daily, perMile, maintenance, total);
    }

    public double calculateAnnualFuelCost(Vehicle vehicle){
        if (vehicle.getFuelType() != null){
            double kwhPerMile = kwhPerGallonEquivalent/vehicle.getCombinedMpg();
            double annualKwh = kwhPerMile * annualMiles;
            return annualKwh * electricityPricePerKWH;
        } else {
            double gallonsUsed = annualMiles / vehicle.getCombinedMpg();
            return gallonsUsed * annualGasPrice;
        }
    }
    public double calculateCostPerMile(Vehicle vehicle){
        return calculateAnnualFuelCost(vehicle) / annualMiles;
    }
    public double calculateMonthlyFuelCost(Vehicle vehicle){
        return calculateAnnualFuelCost(vehicle) / 12;
    }
    public double calculateWeeklyFuelCost(Vehicle vehicle){
        return calculateDailyFuelCost(vehicle) * 7;
    }
    public double calculateDailyFuelCost(Vehicle vehicle){
        return calculateAnnualFuelCost(vehicle) / 365;
    }

    public double calculateYearsOwnedFuelCost(Vehicle vehicle){
        return calculateAnnualFuelCost(vehicle) * yearsOwned;
    }

    public double calculateOneYearSavings(Vehicle vehicle1, Vehicle vehicle2) {
        double cost1 = calculateAnnualFuelCost(vehicle1);
        double cost2 = calculateAnnualFuelCost(vehicle2);
        return Math.abs(cost1 - cost2);
    }

    public double calculateYearSavings(Vehicle vehicle1, Vehicle vehicle2){
        return calculateOneYearSavings(vehicle1,vehicle2) * yearsOwned;
    }

    public double calculateYearlyMaintenance(Vehicle vehicle, int annualMiles){
        double baseCost;
        double mileageFactor;

        if (vehicle.getFuelType() != null && vehicle.getFuelType().equals("Electricity")){
            baseCost = 400;
            mileageFactor = 0.03;
        } else if (vehicle.getCombinedMpg() > 30){
            baseCost = 550;
            mileageFactor = 0.045;
        } else{
            baseCost = 650;
            mileageFactor = 0.055;
        }

        int age = Year.now().getValue() - vehicle.getYear();
        double ageFactor = 1.0 + (age * 0.08);

        return (baseCost + (mileageFactor *annualMiles)) * ageFactor;
    }

    public String getMoreEfficientVehicle(Vehicle vehicle1, Vehicle vehicle2) {
        if (vehicle1.getCombinedMpg() > vehicle2.getCombinedMpg()) {
            return vehicle1.getMake() + " " + vehicle1.getModel();
        } else if (vehicle2.getCombinedMpg() > vehicle1.getCombinedMpg()) {
            return vehicle2.getMake() + " " + vehicle2.getModel();
        } else {
            return "Both vehicles have the same efficiency";
        }
    }

    public void displayVehicleDetails(Vehicle vehicle, TextArea resultArea) {
        String details;
        if (vehicle.getFuelType() != null && vehicle.getFuelType().equals("Electricity")) {
            details = String.format(
                    """
                    Type: %s
                    City: %.1f MPGe
                    Hwy: %.1f MPGe
                    Comb: %.1f MPGe
                    kWh/100mi: %.1f""",
                    vehicle.getFuelType(),
                    vehicle.getCityMpg(),
                    vehicle.getHighwayMpg(),
                    vehicle.getCombinedMpg(),
                    vehicle.getCombinedMpge()
            );
        } else {
            details = String.format(
                    """
                    Type: Gasoline
                    City: %.1f MPG
                    Hwy: %.1f MPG
                    Comb: %.1f MPG""",
                    vehicle.getCityMpg(),
                    vehicle.getHighwayMpg(),
                    vehicle.getCombinedMpg()
            );
        }
        resultArea.setText(details);
    }

    public void updateLoanAmount(TextField purchaseField, TextField downPaymentField, TextField loanField) {
        try {
            double purchasePrice = parseDouble(purchaseField.getText());
            double downPayment = parseDouble(downPaymentField.getText());
            double loanAmount = purchasePrice - downPayment;

            if (loanAmount < 0) {
                loanAmount = 0;
            }

            loanField.setText(String.format("%.2f", loanAmount));
        } catch (NumberFormatException e) {
            loanField.setText("");
        }
    }

    private double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(value);
    }
    public String getTooltipText(int row, ComparisonResult result) {
        Vehicle vehicle1 = result.vehicle1();
        Vehicle vehicle2 = result.vehicle2();
        double fuelPrice = this.annualGasPrice;
        int annualMiles = this.annualMiles;
        int ownershipYears = this.yearsOwned;
        int currentYear = Year.now().getValue();

        ComparisonResult.CostBreakdown cost1 = result.cost1();
        ComparisonResult.CostBreakdown cost2 = result.cost2();

        return switch (row) {
            case 1 -> String.format("""
                Fuel Cost per Mile Calculation:
                
                Vehicle 1: %s
                $%.2f / %.1f MPG = $%.4f/mile
                
                Vehicle 2: %s
                $%.2f / %.1f MPG = $%.4f/mile""",
                    vehicle1,
                    fuelPrice, vehicle1.getCombinedMpg(), cost1.perMile(),
                    vehicle2,
                    fuelPrice, vehicle2.getCombinedMpg(), cost2.perMile());

            case 2 -> String.format("""
                Daily Fuel Cost Calculation:
                
                Vehicle 1: ($%.2f annual / 365 days) = $%.2f/day
                
                Vehicle 2: ($%.2f annual / 365 days) = $%.2f/day""",
                    cost1.annual(), cost1.daily(),
                    cost2.annual(), cost2.daily());

            case 3 -> String.format("""
                Weekly Fuel Cost Calculation:
                
                Vehicle 1: ($%.2f daily * 7 days) = $%.2f/week
                
                Vehicle 2: ($%.2f daily * 7 days) = $%.2f/week""",
                    cost1.daily(), cost1.weekly(),
                    cost2.daily(), cost2.weekly());

            case 4 -> String.format("""
                Monthly Fuel Cost Calculation:
                
                Vehicle 1: ($%.2f annual / 12 months) = $%.2f/month
                
                Vehicle 2: ($%.2f annual / 12 months) = $%.2f/month""",
                    cost1.annual(), cost1.monthly(),
                    cost2.annual(), cost2.monthly());

            case 5 -> String.format("""
                Annual Fuel Cost Calculation:
                
                Vehicle 1: %d miles / %.1f MPG * $%.2f/gal = $%.2f
                
                Vehicle 2: %d miles / %.1f MPG * $%.2f/gal = $%.2f""",
                    annualMiles, vehicle1.getCombinedMpg(), fuelPrice, cost1.annual(),
                    annualMiles, vehicle2.getCombinedMpg(), fuelPrice, cost2.annual());

            case 6 -> String.format("""
                Ownership Period Fuel Cost:
                
                Vehicle 1: $%.2f annual * %d years = $%.2f
                
                Vehicle 2: $%.2f annual * %d years = $%.2f""",
                    cost1.annual(), ownershipYears, cost1.annual() * ownershipYears,
                    cost2.annual(), ownershipYears, cost2.annual() * ownershipYears);

            case 7 -> {
                ComparisonResult.MaintenanceCalculation maint1 = ComparisonResult.calculateMaintenanceDetails(vehicle1, annualMiles);
                ComparisonResult.MaintenanceCalculation maint2 = ComparisonResult.calculateMaintenanceDetails(vehicle2, annualMiles);

                yield String.format("""
                    Annual Maintenance Cost Breakdown:
                    
                    Vehicle 1: %s
                    - Base Cost: $%.2f
                    - Mileage Cost: $%.2f (%.3f * %d miles)
                    - Vehicle Age: %d years
                    - Age Factor: %.2fx
                    --------------------------
                    TOTAL: ($%.2f + $%.2f) * %.2f = $%.2f
                    
                    Vehicle 2: %s
                    - Base Cost: $%.2f
                    - Mileage Cost: $%.2f (%.3f * %d miles)
                    - Vehicle Age: %d years
                    - Age Factor: %.2fx
                    --------------------------
                    TOTAL: ($%.2f + $%.2f) * %.2f = $%.2f""",
                        vehicle1,
                        maint1.baseCost(),
                        maint1.mileageCost(), maint1.mileageFactor(), annualMiles,
                        maint1.age(),
                        maint1.ageFactor(),
                        maint1.baseCost(), maint1.mileageCost(), maint1.ageFactor(), cost1.maintenance(),
                        vehicle2,
                        maint2.baseCost(),
                        maint2.mileageCost(), maint2.mileageFactor(), annualMiles,
                        maint2.age(),
                        maint2.ageFactor(),
                        maint2.baseCost(), maint2.mileageCost(), maint2.ageFactor(), cost2.maintenance());
            }

            case 8 -> {
                double ageFactor1 = 1.0 + ((currentYear - vehicle1.getYear()) * 0.08);
                double ageFactor2 = 1.0 + ((currentYear - vehicle2.getYear()) * 0.08);

                yield String.format("""
                    Total Ownership Cost (%d years):
                    
                    Vehicle 1: %s
                    - Purchase: $%.2f
                    - Down Payment: $%.2f
                    - Loan: $%.2f @ %.2f%% for %.0f months
                    - Total Interest: $%.2f
                    - Fuel: $%.2f
                    - Maintenance: $%.2f
                      (Age Factor: %.2fx)
                    --------------------------
                    TOTAL: $%.2f
                    
                    Vehicle 2: %s
                    - Purchase: $%.2f
                    - Down Payment: $%.2f
                    - Loan: $%.2f @ %.2f%% for %.0f months
                    - Total Interest: $%.2f
                    - Fuel: $%.2f
                    - Maintenance: $%.2f
                      (Age Factor: %.2fx)
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
                        cost1.annual() * ownershipYears,
                        cost1.maintenance() * ownershipYears,
                        ageFactor1,
                        cost1.total(),
                        vehicle2,
                        vehicle2.getPurchasePrice(),
                        vehicle2.getDownPayment(),
                        vehicle2.getLoanAmount(),
                        vehicle2.getInterestRate(),
                        vehicle2.getLoanPeriod(),
                        vehicle2.calculateTotalInterest(),
                        cost2.annual() * ownershipYears,
                        cost2.maintenance() * ownershipYears,
                        ageFactor2,
                        cost2.total());
            }

            default -> String.format("""
                Vehicle 1: $%.2f
                Vehicle 2: $%.2f""",
                    getValueForRow(row, result, true),
                    getValueForRow(row, result, false));
        };
    }

    private double getValueForRow(int row, ComparisonResult result, boolean isVehicle1) {
        ComparisonResult.CostBreakdown cost = isVehicle1 ? result.cost1() : result.cost2();
        return switch (row) {
            case 1 -> cost.perMile();
            case 2 -> cost.daily();
            case 3 -> cost.weekly();
            case 4 -> cost.monthly();
            case 5 -> cost.annual();
            case 6 -> cost.annual() * this.yearsOwned;
            case 7 -> cost.maintenance();
            case 8 -> cost.total();
            default -> 0.0;
        };
    }
}
