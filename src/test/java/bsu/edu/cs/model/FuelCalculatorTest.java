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
    public void testDefaultConstructorValues() {
        assertEquals(3.50, calculator.getAnnualGasPrice(), 0.001);
        assertEquals(15000, calculator.getAnnualMiles());
        assertEquals(5, calculator.getYearsOwned());
        assertEquals(0.13, calculator.getElectricityPricePerKWH(), 0.001);
    }

    @Test
    public void testCalculateAnnualFuelCost_GasVehicle() {
        double expectedGallons = 15000 / 30.0;
        double expectedCost = expectedGallons * 3.50;

        assertEquals(expectedCost, calculator.calculateAnnualFuelCost(gasVehicle), 0.01);
    }

    @Test
    public void testCalculateAnnualFuelCost_ElectricVehicle() {
        double kwhPerMile = 33.705 / 120.0;
        double annualKwh = kwhPerMile * 15000;
        double expectedCost = annualKwh * 0.13;

        assertEquals(expectedCost, calculator.calculateAnnualFuelCost(electricVehicle), 0.01);
    }

    @Test
    public void testCalculateYearsOwnedFuelCost() {
        double annualCost = calculator.calculateAnnualFuelCost(gasVehicle);
        double expectedTotalCost = annualCost * 5;

        assertEquals(expectedTotalCost, calculator.calculateYearsOwnedFuelCost(gasVehicle), 0.01);
    }

    @Test
    public void testCalculateOneYearSavings() {
        Vehicle efficientVehicle = new Vehicle("Honda", "Insight", 52.0, 2023);
        double savings = calculator.calculateOneYearSavings(gasVehicle, efficientVehicle);
        assertTrue(savings > 0);
    }

    @Test
    public void testGetMoreEfficientVehicle() {
        Vehicle efficientVehicle = new Vehicle("Honda", "Insight", 52.0, 2023);
        String result = calculator.getMoreEfficientVehicle(gasVehicle, efficientVehicle);
        assertEquals("Honda Insight", result);
    }

    @Test
    public void testSetElectricityPrice() {
        calculator.setElectricityPricePerKWH(0.15);
        assertEquals(0.15, calculator.getElectricityPricePerKWH(), 0.001);
    }
}