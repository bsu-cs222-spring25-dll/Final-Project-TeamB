package bsu.edu.cs.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VehicleTest {

    @Test
    public void constructorSetsMakeCorrectly() {
        Vehicle vehicle = new Vehicle("Toyota", "Camry", "LE", 30.0, 28.0, 32.0, "2023");
        assertEquals("Toyota", vehicle.getMake());
    }

    @Test
    public void constructorSetsModelCorrectly() {
        Vehicle vehicle = new Vehicle("Toyota", "Camry", "LE", 30.0, 28.0, 32.0, "2023");
        assertEquals("Camry", vehicle.getModel());
    }

    @Test
    public void constructorSetsTrimCorrectly() {
        Vehicle vehicle = new Vehicle("Toyota", "Camry", "LE", 30.0, 28.0, 32.0, "2023");
        assertEquals("LE", vehicle.getTrim());
    }

    @Test
    public void constructorSetsCombinedMpgCorrectly() {
        Vehicle vehicle = new Vehicle("Toyota", "Camry", "LE", 30.0, 28.0, 32.0, "2023");
        assertEquals(30.0, vehicle.getCombinedMpg(), 0.001);
    }

    @Test
    public void constructorSetsYearCorrectly() {
        Vehicle vehicle = new Vehicle("Toyota", "Camry", "LE", 30.0, 28.0, 32.0, "2023");
        assertEquals(2023, vehicle.getYear());
    }

    @Test
    public void electricVehicleConstructorSetsFuelTypeCorrectly() {
        Vehicle ev = new Vehicle("Tesla", "Model 3", "Standard", 120.0, 110.0, 130.0,
                "2023", "Electricity", 120.0);
        assertEquals("Electricity", ev.getFuelType());
    }

    @Test
    public void toStringReturnsExpectedFormat() {
        Vehicle vehicle = new Vehicle("Honda", "Civic", "EX", 35.0, 32.0, 38.0, "2022");
        assertEquals("2022 Honda Civic EX", vehicle.toString());
    }

    @Test
    public void simplifiedConstructorCalculatesCityMpgCorrectly() {
        Vehicle vehicle = new Vehicle("Ford", "F-150", 22.0, 2021);
        assertEquals(19.8, vehicle.getCityMpg(), 0.001);
    }

    @Test
    public void simplifiedConstructorCalculatesHighwayMpgCorrectly() {
        Vehicle vehicle = new Vehicle("Ford", "F-150", 22.0, 2021);
        assertEquals(24.2, vehicle.getHighwayMpg(), 0.001);
    }
    @Test
    public void calculateMonthlyPaymentReturnsCorrectValue() {
        Vehicle vehicle = new Vehicle("Toyota", "Camry", 30.0, 2023);
        vehicle.setLoanAmount(20000);
        vehicle.setInterestRate(5.0);
        vehicle.setLoanPeriod(60);

        double expected = 377.42;
        assertEquals(expected, vehicle.calculateMonthlyPayment(), 0.01);
    }

    @Test
    public void calculateTotalInterestReturnsCorrectValue() {
        Vehicle vehicle = new Vehicle("Toyota", "Camry", 30.0, 2023);
        vehicle.setLoanAmount(20000);
        vehicle.setInterestRate(5.0);
        vehicle.setLoanPeriod(60);

        double expected = 2645.48;
        assertEquals(expected, vehicle.calculateTotalInterest(), 0.01);
    }

    @Test
    public void calculateTotalCostOfOwnershipForGasVehicle() {
        Vehicle vehicle = new Vehicle("Toyota", "Camry", 30.0, 2023);
        vehicle.setPurchasePrice(25000);
        double totalCost = vehicle.calculateTotalCostOfOwnership(15000, 5, 3.50, 0.13);
        assertTrue(totalCost > 25000);
    }

    @Test
    public void calculateTotalCostOfOwnershipForElectricVehicle() {
        Vehicle ev = new Vehicle("Tesla", "Model 3", "Standard", 120.0, 110.0, 130.0,
                "2023", "Electricity", 120.0);
        ev.setPurchasePrice(45000);
        double totalCost = ev.calculateTotalCostOfOwnership(15000, 5, 3.50, 0.13);
        assertTrue(totalCost > 45000);
    }
}