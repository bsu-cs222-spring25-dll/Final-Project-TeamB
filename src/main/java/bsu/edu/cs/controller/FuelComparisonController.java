package bsu.edu.cs.controller;

import bsu.edu.cs.model.ComparisonResult;
import bsu.edu.cs.model.Vehicle;

public interface FuelComparisonController {
    ComparisonResult compareVehicles(Vehicle vehicle1, Vehicle vehicle2);
    void updateCalculatorSettings(double gasPrice, int miles, int years, double electricityPrice);
    Vehicle createVehicleFromMpg(double mpg, String name);
    void updateSettingsFromStrings(String gasPriceStr, String milesStr, String yearsStr, String electricityStr);
    Vehicle createVehicleFromInputs(String trim, String number, Vehicle selectedVehicle);
}