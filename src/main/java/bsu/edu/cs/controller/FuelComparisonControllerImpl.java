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

    private Vehicle currentVehicle1;
    private Vehicle currentVehicle2;

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

    @Override
    public void updateSettingsFromStrings(String gasPriceStr, String milesStr, String yearsStr, String electricityStr) {
        try {
            double gasPrice = Double.parseDouble(gasPriceStr);
            int miles = Integer.parseInt(milesStr);
            int years = Integer.parseInt(yearsStr);
            double electricityPrice = Double.parseDouble(electricityStr);

            updateCalculatorSettings(gasPrice, miles, years, electricityPrice);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input values. Please enter valid numbers.");
        }
    }

    public Vehicle createVehicleFromInputs(String mpgStr, String name, Vehicle selectedVehicle) {
        if (mpgStr != null && !mpgStr.isEmpty()) {
            return createVehicleFromMpg(Double.parseDouble(mpgStr), name);
        } else if (selectedVehicle != null) {
            return selectedVehicle;
        }
        throw new IllegalArgumentException("No valid vehicle input provided.");
    }
    @Override
    public void updateFinancialSettings(
            double purchasePrice1, double downPayment1, double loanAmount1, double interestRate1, double loanPeriod1,
            double purchasePrice2, double downPayment2, double loanAmount2, double interestRate2, double loanPeriod2) {

        if (currentVehicle1 != null) {
            updateVehicleFinancials(currentVehicle1, purchasePrice1, downPayment1, loanAmount1, interestRate1, loanPeriod1);
        }

        if (currentVehicle2 != null) {
            updateVehicleFinancials(currentVehicle2, purchasePrice2, downPayment2, loanAmount2, interestRate2, loanPeriod2);
        }

        if (currentVehicle1 != null && currentVehicle2 != null) {
            compareVehicles(currentVehicle1, currentVehicle2);
        }
    }

    private void updateVehicleFinancials(Vehicle vehicle, double purchasePrice, double downPayment,
                                         double loanAmount, double interestRate, double loanPeriod) {
        vehicle.setPurchasePrice(purchasePrice);
        vehicle.setDownPayment(downPayment);
        vehicle.setLoanAmount(loanAmount);
        vehicle.setInterestRate(interestRate);
        vehicle.setLoanPeriod(loanPeriod);
    }
}