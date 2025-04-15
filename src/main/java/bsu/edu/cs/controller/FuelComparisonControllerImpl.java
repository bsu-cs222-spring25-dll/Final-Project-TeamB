package bsu.edu.cs.controller;

import bsu.edu.cs.model.ComparisonResult;
import bsu.edu.cs.model.FuelCalculator;
import bsu.edu.cs.model.FuelEconomyService;
import bsu.edu.cs.model.Vehicle;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FuelComparisonControllerImpl implements FuelComparisonController {
    private final FuelCalculator calculator;
    private final FuelEconomyService fuelEconomyService;
    private static final double MAX_MPG = 150.0;

    public FuelComparisonControllerImpl(FuelCalculator calculator, FuelEconomyService fuelEconomyService) {
        this.calculator = calculator;
        this.fuelEconomyService = fuelEconomyService;
    }

    @Override
    public CompletableFuture<List<String>> getYears() {
        return fuelEconomyService.getYears();
    }

    @Override
    public CompletableFuture<List<String>> getMakes(String year) {
        return fuelEconomyService.getMakes(year);
    }

    @Override
    public CompletableFuture<List<String>> getModels(String year, String make) {
        return fuelEconomyService.getModels(year, make);
    }

    @Override
    public CompletableFuture<List<String>> getTrims(String year, String make, String model) {
        return fuelEconomyService.getTrims(year, make, model);
    }

    @Override
    public CompletableFuture<Vehicle> searchVehicle(String year, String make, String model, String trim) {
        return fuelEconomyService.searchVehicles(year, make, model, trim);
    }
    @Override
    public ComparisonResult compareVehicles(Vehicle vehicle1, Vehicle vehicle2) {
        if (vehicle1 == null || vehicle2 == null) {
            throw new IllegalArgumentException("Both vehicles must be provided.");
        }
        return calculator.compareVehicles(vehicle1, vehicle2);
    }

    @Override
    public void updateCalculatorSettings(double gasPrice, int miles, int years, double electricityPrice) {
        if (gasPrice <= 0 || miles <= 0 || years <= 0 || electricityPrice <= 0) {
            throw new IllegalArgumentException("All values must be positive.");
        }
        calculator.setAnnualGasPrice(gasPrice);
        calculator.setAnnualMiles(miles);
        calculator.setYearsOwned(years);
        calculator.setElectricityPricePerKWH(electricityPrice);
    }

    @Override
    public Vehicle createVehicleFromMpg(double mpg, String name) {
        if (mpg <= 0 || mpg > MAX_MPG) {
            throw new IllegalArgumentException("MPG must be between 0 and " + MAX_MPG);
        }
        return new Vehicle("Custom Vehicle", name, mpg, 2023);
    }

    public Vehicle createVehicleFromInputs(String mpgStr, String name, Vehicle selectedVehicle) {
        if (mpgStr != null && !mpgStr.isEmpty()) {
            return createVehicleFromMpg(Double.parseDouble(mpgStr), name);
        } else if (selectedVehicle != null) {
            return selectedVehicle;
        }
        throw new IllegalArgumentException("No valid vehicle input provided.");
    }

    public void updateSettingsFromStrings(String gasPriceStr, String milesStr, String yearsStr, String electricityStr) {
        try {
            double gasPrice = gasPriceStr.isEmpty() ? 3.50 : Double.parseDouble(gasPriceStr);
            int miles = milesStr.isEmpty() ? 15000 : Integer.parseInt(milesStr);
            int years = yearsStr.isEmpty() ? 5 : Integer.parseInt(yearsStr);
            double electricityPrice = electricityStr.isEmpty() ? 0.13 : Double.parseDouble(electricityStr);
            updateCalculatorSettings(gasPrice, miles, years, electricityPrice);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input format. Please enter valid numbers.");
        }
    }
}