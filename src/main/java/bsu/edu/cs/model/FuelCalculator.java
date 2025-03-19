package bsu.edu.cs.model;

public class FuelCalculator {
    private double annualGasPrice;
    private int annualMiles;
    private int yearsOwned;

    public FuelCalculator() {
        this.annualGasPrice = 3.50;
        this.annualMiles = 15000;
        this.yearsOwned = 5;
    }

    public Double getAnnualGasPrice() {
        return annualGasPrice;
    }

    public void setAnnualGasPrice(double annualGasPrice) {
        this.annualGasPrice = annualGasPrice;
    }

    public Integer getAnnualMiles() {
        return annualMiles;
    }

    public void setAnnualMiles(int annualMiles) {
        this.annualMiles = annualMiles;
    }

    public int getYearsOwned() {
        return yearsOwned;
    }

    public void setYearsOwned(int yearsOwned) {
        this.yearsOwned = yearsOwned;
    }


    public double calculateAnnualFuelCost(Vehicle vehicle){
        double gallonsUsed = annualMiles / vehicle.getCombinedMpg();
        return gallonsUsed * annualGasPrice;
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
