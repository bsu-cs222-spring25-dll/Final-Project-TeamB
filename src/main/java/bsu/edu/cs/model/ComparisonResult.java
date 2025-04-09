package bsu.edu.cs.model;

public record ComparisonResult(Vehicle vehicle1, Vehicle vehicle2, double annualCost1, double annualCost2,
                               double yearCost1, double yearCost2, double monthCost1, double monthCost2, double weekCost1, double weekCost2, double dayCost1, double dayCost2, double perMileCost1, double perMileCost2, double annualSavings, int yearsOwned,
                               double yearsSavings, String moreEfficientVehicle) {
}