package bsu.edu.cs.controller;

import bsu.edu.cs.model.ComparisonResult;
import bsu.edu.cs.model.FuelCalculator;
import bsu.edu.cs.model.Vehicle;

public class FuelComparisonControllerImpl implements FuelComparisonController {
    private final FuelCalculator calculator;

    public FuelComparisonControllerImpl(FuelCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public ComparisonResult compareVehicles(Vehicle vehicle1, Vehicle vehicle2) {
        return calculator.compareVehicles(vehicle1, vehicle2);
    }

    @Override
    public void updateCalculatorSettings(double gasPrice, int miles, int years, double electricityPrice) {
        calculator.setAnnualGasPrice(gasPrice);
        calculator.setAnnualMiles(miles);
        calculator.setYearsOwned(years);
        calculator.setElectricityPricePerKWH(electricityPrice);
    }

    @Override
    public Vehicle createVehicleFromMpg(double mpg, String name) {
        return new Vehicle("Vehicle", name, mpg, 2023);
    }
}