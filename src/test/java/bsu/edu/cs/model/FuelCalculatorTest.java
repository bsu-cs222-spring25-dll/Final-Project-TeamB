package bsu.edu.cs.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FuelCalculatorTest {
    private FuelCalculator calculator;
    private Vehicle highEfficiencyVehicle;
    private Vehicle lowEfficiencyVehicle;

    @BeforeEach
    public void setUp(){
        calculator = new FuelCalculator();
        highEfficiencyVehicle = new Vehicle("Toyota","Prius",56, 2024);
        lowEfficiencyVehicle = new Vehicle("Chevrolet","Silverado 1500",16, 2004);
    }

    @Test
    public void calculateAnnualFuelCostTest(){
        double priusCost = calculator.calculateAnnualFuelCost(highEfficiencyVehicle);
        assertEquals(937.50,priusCost,0.01);
        double silveradoCost = calculator.calculateAnnualFuelCost(lowEfficiencyVehicle);
        assertEquals(937.50,silveradoCost,0.01);
    }

    @Test
    public void calculateOneYearSavingsTest(){
        double savings = calculator.calculateOneYearSavings(highEfficiencyVehicle,lowEfficiencyVehicle);
        assertEquals(2343.75, savings, 0.01);
        double savingsReversed = calculator.calculateOneYearSavings(lowEfficiencyVehicle,highEfficiencyVehicle);
        assertEquals(2343.75, savingsReversed, 0.01);
    }
    @Test
    public void calculateYearSavingsTest(){
        double YearSavings = calculator.calculateYearSavings(highEfficiencyVehicle,lowEfficiencyVehicle);
        assertEquals(11718.75,YearSavings,0.01);
    }
    @Test
    public void getMoreEfficientVehicleTest(){
        String moreEfficient = calculator.getMoreEfficientVehicle(highEfficiencyVehicle,lowEfficiencyVehicle);
        assertEquals(highEfficiencyVehicle.getMake() + " " + highEfficiencyVehicle.getModel(),moreEfficient);
    }
    @Test
    public void SameEfficiencyTest(){
        Vehicle vehicle1 = new Vehicle("Honda","Accord",31, 2);
        Vehicle vehicle2 = new Vehicle("Honda","Civic",31, 2);
        String moreEfficient = calculator.getMoreEfficientVehicle(vehicle1,vehicle2);
        assertEquals("Both vehicles have the same efficiency",moreEfficient);
    }
}
