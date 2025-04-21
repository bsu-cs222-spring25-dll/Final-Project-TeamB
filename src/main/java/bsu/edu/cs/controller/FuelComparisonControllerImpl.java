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

    public void updateSettingsFromStrings(String gasPriceStr, String milesStr, String timeStr, String electricStr) {
        try {
            double gasPrice = Double.parseDouble(gasPriceStr);
            int miles = Integer.parseInt(milesStr);
            int time = Integer.parseInt(timeStr);
            double electric = Double.parseDouble(electricStr);

            if (gasPrice <= 0 || miles <= 0 || time <= 0 || electric <= 0) {
                throw new IllegalArgumentException("All values must be positive");
            }

            calculator.updateAllParameters(gasPrice, miles, time, electric);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input: Please enter valid numbers");
        }
    }

    public Vehicle createVehicleFromInputs(String mpgStr, String name, Vehicle selectedVehicle) {
        if (mpgStr != null && !mpgStr.isEmpty()) {
            return createVehicleFromMpg(Double.parseDouble(mpgStr), name);
        } else if (selectedVehicle != null) {
            System.out.println(selectedVehicle);
            return selectedVehicle;
        }
        throw new IllegalArgumentException("No valid vehicle input provided.");
    }
    @Override
    public void updateFinancialSettings(
            double purchasePrice1, double downPayment1, double loanAmount1, double interestRate1, double loanPeriod1,
            double purchasePrice2, double downPayment2, double loanAmount2, double interestRate2, double loanPeriod2) {

        if (currentVehicle1 != null) {
            currentVehicle1.setPurchasePrice(purchasePrice1);
            currentVehicle1.setDownPayment(downPayment1);
            currentVehicle1.setLoanAmount(loanAmount1);
            currentVehicle1.setInterestRate(interestRate1);
            currentVehicle1.setLoanPeriod(loanPeriod1);
        }

        if (currentVehicle2 != null) {
            currentVehicle2.setPurchasePrice(purchasePrice2);
            currentVehicle2.setDownPayment(downPayment2);
            currentVehicle2.setLoanAmount(loanAmount2);
            currentVehicle2.setInterestRate(interestRate2);
            currentVehicle2.setLoanPeriod(loanPeriod2);
        }

        if (currentVehicle1 != null && currentVehicle2 != null) {
            compareVehicles(currentVehicle1, currentVehicle2);
        }
    }

    public void setCurrentVehicles(Vehicle vehicle1, Vehicle vehicle2) {
        this.currentVehicle1 = vehicle1;
        this.currentVehicle2 = vehicle2;
    }
}