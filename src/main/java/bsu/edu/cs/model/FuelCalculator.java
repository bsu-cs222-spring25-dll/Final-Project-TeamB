package bsu.edu.cs.model;

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
        double annualCost1 = calculateAnnualFuelCost(vehicle1);
        double annualCost2 = calculateAnnualFuelCost(vehicle2);
        double yearCost1 = calculateYearsOwnedFuelCost(vehicle1);
        double yearCost2 = calculateYearsOwnedFuelCost(vehicle2);
        double monthCost1 = calculateMonthlyFuelCost(vehicle1);
        double monthCost2 = calculateMonthlyFuelCost(vehicle2);
        double weekCost1 = calculateWeeklyFuelCost(vehicle1);
        double weekCost2 = calculateWeeklyFuelCost(vehicle2);
        double dayCost1 = calculateDailyFuelCost(vehicle1);
        double dayCost2 = calculateDailyFuelCost(vehicle2);
        double perMileCost1 = calculateCostPerMile(vehicle1);
        double perMileCost2 = calculateCostPerMile(vehicle2);
        double annualSavings = calculateOneYearSavings(vehicle1, vehicle2);
        double yearsSavings = calculateYearSavings(vehicle1, vehicle2);
        String moreEfficient = getMoreEfficientVehicle(vehicle1, vehicle2);
        double maintenanceCost1 = calculateYearlyMaintenance(vehicle1, annualMiles);
        double maintenanceCost2 = calculateYearlyMaintenance(vehicle2, annualMiles);
        double totalCost1= vehicle1.calculateTotalCostOfOwnership(annualMiles,yearsOwned,annualGasPrice,electricityPricePerKWH);
        double totalCost2= vehicle2.calculateTotalCostOfOwnership(annualMiles,yearsOwned,annualGasPrice,electricityPricePerKWH);
        return new ComparisonResult(vehicle1, vehicle2,
                annualCost1, annualCost2,
                yearCost1, yearCost2,
                annualSavings,
                yearsOwned,
                yearsSavings,
                moreEfficient,
                monthCost1,
                monthCost2,
                weekCost1,
                weekCost2,
                dayCost1,
                dayCost2,
                perMileCost1,
                perMileCost2,
                maintenanceCost1,
                maintenanceCost2,
                totalCost1,
                totalCost2);
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


}
