package bsu.edu.cs.model;

import java.time.Year;

public record ComparisonResult(
        Vehicle vehicle1,
        Vehicle vehicle2,
        CostBreakdown cost1,
        CostBreakdown cost2,
        double annualSavings,
        int yearsOwned,
        double yearsSavings,
        String moreEfficientVehicle
) {
    public record CostBreakdown(
            double annual,
            double monthly,
            double weekly,
            double daily,
            double perMile,
            double maintenance,
            double total
    ) {}

    public static MaintenanceCalculation calculateMaintenanceDetails(Vehicle vehicle, int annualMiles) {
        double baseCost;
        double mileageFactor;

        if (vehicle.getFuelType() != null && vehicle.getFuelType().equals("Electricity")) {
            baseCost = 400;
            mileageFactor = 0.03;
        } else if (vehicle.getCombinedMpg() > 30) {
            baseCost = 550;
            mileageFactor = 0.045;
        } else {
            baseCost = 650;
            mileageFactor = 0.055;
        }

        int age = Year.now().getValue() - vehicle.getYear();
        double ageFactor = 1.0 + (age * 0.08);
        double mileageCost = mileageFactor * annualMiles;
        double totalMaintenance = (baseCost + mileageCost) * ageFactor;

        return new MaintenanceCalculation(baseCost, mileageFactor, mileageCost, age, ageFactor, totalMaintenance);
    }

    public record MaintenanceCalculation(
            double baseCost,
            double mileageFactor,
            double mileageCost,
            int age,
            double ageFactor,
            double totalMaintenance
    ) {}
}