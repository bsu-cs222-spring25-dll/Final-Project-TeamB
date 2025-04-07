package bsu.edu.cs.model;

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

    public Double getAnnualGasPrice() {
        return annualGasPrice;
    }
    public void setAnnualGasPrice(double annualGasPrice) {
        this.annualGasPrice = annualGasPrice;
    }

    public void setAnnualMiles(int annualMiles) {
        this.annualMiles = annualMiles;
    }

    public void setYearsOwned(int yearsOwned) {
        this.yearsOwned = yearsOwned;
    }

    public void setElectricityPricePerKWH(double electricityPricePerKWH){ this.electricityPricePerKWH = electricityPricePerKWH;}

    public ComparisonResult compareVehicles(Vehicle vehicle1, Vehicle vehicle2) {
        double annualCost1 = calculateAnnualFuelCost(vehicle1);
        double annualCost2 = calculateAnnualFuelCost(vehicle2);
        double yearCost1 = calculateYearsOwnedFuelCost(vehicle1);
        double yearCost2 = calculateYearsOwnedFuelCost(vehicle2);
        double annualSavings = calculateOneYearSavings(vehicle1, vehicle2);
        double yearsSavings = calculateYearSavings(vehicle1, vehicle2);
        String moreEfficient = getMoreEfficientVehicle(vehicle1, vehicle2);

        return new ComparisonResult(vehicle1, vehicle2, annualCost1, annualCost2,
                yearCost1, yearCost2, annualSavings, yearsOwned, yearsSavings,
                moreEfficient);
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
