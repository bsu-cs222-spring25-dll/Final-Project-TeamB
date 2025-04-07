package bsu.edu.cs.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FuelCalculatorTest {

    private FuelCalculator calculator;
    private Vehicle gasVehicle;
    private Vehicle electricVehicle;

    @BeforeEach
    public void setUp() {
        calculator = new FuelCalculator();
        gasVehicle = new Vehicle("Toyota", "Camry", 30.0, 2023);
        electricVehicle = new Vehicle("Tesla", "Model 3","Model 3 Long Range AWD", 120.0, 110.0, 130.0,
                "2023", "Electricity", 120.0);
    }

    @Test
    public void defaultAnnualGasPriceIsSetCorrectly() {
        assertEquals(3.50, calculator.getAnnualGasPrice(), 0.001);
    }

    @Test
    public void setAnnualGasPriceUpdatesValue() {
        calculator.setAnnualGasPrice(4.00);
        assertEquals(4.00, calculator.getAnnualGasPrice(), 0.001);
    }

    @Test
    public void calculateAnnualFuelCostForGasVehicleUsesCorrectFormula() {
        double expectedGallons = 15000 / 30.0;
        double expectedCost = expectedGallons * 3.50;
        assertEquals(expectedCost, calculator.calculateAnnualFuelCost(gasVehicle), 0.01);
    }

    @Test
    public void calculateAnnualFuelCostForElectricVehicleUsesCorrectFormula() {
        double kwhPerMile = 33.705 / 120.0;
        double annualKwh = kwhPerMile * 15000;
        double expectedCost = annualKwh * 0.13;
        assertEquals(expectedCost, calculator.calculateAnnualFuelCost(electricVehicle), 0.01);
    }

    @Test
    public void calculateYearsOwnedFuelCostMultipliesAnnualCost() {
        double annualCost = calculator.calculateAnnualFuelCost(gasVehicle);
        assertEquals(annualCost * 5, calculator.calculateYearsOwnedFuelCost(gasVehicle), 0.01);
    }

    @Test
    public void getMoreEfficientVehicleReturnsVehicleWithHigherMpg() {
        Vehicle efficientVehicle = new Vehicle("Honda", "Insight", 52.0, 2023);
        assertEquals("Honda Insight", calculator.getMoreEfficientVehicle(gasVehicle, efficientVehicle));
    }
}