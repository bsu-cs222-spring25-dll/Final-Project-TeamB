package bsu.edu.cs;

public class Calculations {
    public static double calculateAnnualFuelCost(double mpg, double yearlyMileage, double avgGallonCost){
        if (mpg <= 0 || yearlyMileage < 0 || avgGallonCost < 0) {
            throw new IllegalArgumentException("Input values must be positive.");
        }
        double gallonsUsedPerYear = yearlyMileage/mpg;
        return gallonsUsedPerYear * avgGallonCost;
    }

    public static void main(String[] args) {
        double mpg = Menu.getMpg();
        double yearlyMileage = Menu.getYearlyMileage();
        double avgGallonCost = Menu.getAvgGallonCost();
        double annualFuelCost = calculateAnnualFuelCost(mpg,yearlyMileage, avgGallonCost);
        System.out.println("Annual Fuel Cost: $" + annualFuelCost);
    }
}
