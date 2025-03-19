package bsu.edu.cs.model;

public class FuelCalculator {
    public double annualGasPrice;
    public double annualMiles;
    public int yearsOwned;

    public FuelCalculator() {
        this.annualGasPrice = 3.50;
        this.annualMiles = 15000;
        this.yearsOwned = 1;
    }

    public double calculateAnnualFuelCost(Vehicle vehicle){
        double gallonsUsed = annualMiles / vehicle.combinedMpg;
        return gallonsUsed * annualGasPrice;
    }

    public double calculateOneYearSavings(Vehicle vehicle1, Vehicle vehicle2) {
        double cost1 = calculateAnnualFuelCost(vehicle1);
        double cost2 = calculateAnnualFuelCost(vehicle2);
        return Math.abs(cost1 - cost2);
    }

    public double calculateYearSavings(Vehicle vehicle1, Vehicle vehicle2){
        double time = yearsOwned;
        if(time > 0){
            return calculateOneYearSavings(vehicle1,vehicle2) * yearsOwned;
        } else{
            return calculateOneYearSavings(vehicle1,vehicle2);
        }
    }

    public String getMoreEfficientVehicle(Vehicle vehicle1, Vehicle vehicle2) {
        if (vehicle1.combinedMpg > vehicle2.combinedMpg) {
            return vehicle1.make + " " + vehicle1.model;
        } else if (vehicle2.combinedMpg > vehicle1.combinedMpg) {
            return vehicle2.make + " " + vehicle2.model;
        } else {
            return "Both vehicles have the same efficiency";
        }
    }

}
