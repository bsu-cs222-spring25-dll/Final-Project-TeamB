package bsu.edu.cs.model;

public record ComparisonResult(Vehicle vehicle1, Vehicle vehicle2, double annualCost1, double annualCost2,
                               double yearCost1, double yearCost2, double annualSavings, int yearsOwned,
                               double yearsSavings, String moreEfficientVehicle) {
}