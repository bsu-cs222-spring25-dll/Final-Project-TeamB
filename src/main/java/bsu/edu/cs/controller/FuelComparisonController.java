package bsu.edu.cs.controller;

import bsu.edu.cs.model.ComparisonResult;
import bsu.edu.cs.model.Vehicle;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FuelComparisonController {
    ComparisonResult compareVehicles(Vehicle vehicle1, Vehicle vehicle2);
    void updateCalculatorSettings(double gasPrice, int miles, int years, double electricityPrice);
    Vehicle createVehicleFromMpg(double mpg, String name);
    void updateSettingsFromStrings(String gasPriceStr, String milesStr, String yearsStr, String electricityStr);
    Vehicle createVehicleFromInputs(String trim, String number, Vehicle selectedVehicle);

    CompletableFuture<List<String>> getYears();
    CompletableFuture<List<String>> getMakes(String year);
    CompletableFuture<List<String>> getModels(String year, String make);
    CompletableFuture<List<String>> getTrims(String year, String make, String model);
    CompletableFuture<Vehicle> searchVehicle(String year, String make, String model, String trim);
}