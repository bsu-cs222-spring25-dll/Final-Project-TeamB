package bsu.edu.cs.model;


import bsu.edu.cs.controller.FuelComparisonControllerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FuelComparisonControllerTest {
    private FuelComparisonControllerImpl controller;
    private FuelCalculator calculator;
    private final Vehicle vehicle1 = new Vehicle("Toyota", "Camry", 30.0, 2023);
    private final Vehicle vehicle2 = new Vehicle("Honda", "Accord", 28.0, 2023);

    @BeforeEach
    public void setUp() {
        calculator = new FuelCalculator();
        controller = new FuelComparisonControllerImpl(calculator);
    }

    @Test
    public void compareVehiclesReturnsNonNullResult() {
        ComparisonResult result = controller.compareVehicles(vehicle1, vehicle2);
        assertNotNull(result);
    }

    @Test
    public void compareVehiclesReturnsCorrectVehicle1() {
        ComparisonResult result = controller.compareVehicles(vehicle1, vehicle2);
        assertEquals(vehicle1, result.vehicle1());
    }

    @Test
    public void compareVehiclesReturnsCorrectVehicle2() {
        ComparisonResult result = controller.compareVehicles(vehicle1, vehicle2);
        assertEquals(vehicle2, result.vehicle2());
    }

    @Test
    public void compareVehiclesReturnsPositiveAnnualCost1() {
        ComparisonResult result = controller.compareVehicles(vehicle1, vehicle2);
        assertTrue(result.annualCost1() > 0);
    }

    @Test
    public void compareVehiclesReturnsPositiveAnnualCost2() {
        ComparisonResult result = controller.compareVehicles(vehicle1, vehicle2);
        assertTrue(result.annualCost2() > 0);
    }

    @Test
    public void compareVehiclesReturnsMoreEfficientVehicle() {
        ComparisonResult result = controller.compareVehicles(vehicle1, vehicle2);
        assertEquals("Toyota Camry", result.moreEfficientVehicle());
    }

    @Test
    public void updateCalculatorSettingsUpdatesGasPrice() {
        controller.updateCalculatorSettings(4.0, 20000, 7, 0.15);
        assertEquals(4.0, calculator.getAnnualGasPrice());
    }

    @Test
    public void updateCalculatorSettingsUpdatesMiles() {
        controller.updateCalculatorSettings(4.0, 20000, 7, 0.15);
        assertEquals(20000, calculator.getAnnualMiles());
    }

    @Test
    public void updateCalculatorSettingsUpdatesYears() {
        controller.updateCalculatorSettings(4.0, 20000, 7, 0.15);
        assertEquals(7, calculator.getYearsOwned());
    }

    @Test
    public void createVehicleFromMpgReturnsNonNullVehicle() {
        Vehicle vehicle = controller.createVehicleFromMpg(35.0, "Test Vehicle");
        assertNotNull(vehicle);
    }

    @Test
    public void createVehicleFromMpgSetsCorrectMake() {
        Vehicle vehicle = controller.createVehicleFromMpg(35.0, "Test Vehicle");
        assertEquals("Vehicle", vehicle.getMake());
    }

    @Test
    public void createVehicleFromMpgSetsCorrectModel() {
        Vehicle vehicle = controller.createVehicleFromMpg(35.0, "Test Vehicle");
        assertEquals("Test Vehicle", vehicle.getModel());
    }

    @Test
    public void createVehicleFromMpgSetsCorrectMpg() {
        Vehicle vehicle = controller.createVehicleFromMpg(35.0, "Test Vehicle");
        assertEquals(35.0, vehicle.getCombinedMpg());
    }

    @Test
    public void createVehicleFromMpgSetsCorrectYear() {
        Vehicle vehicle = controller.createVehicleFromMpg(35.0, "Test Vehicle");
        assertEquals(2023, vehicle.getYear());
    }

    @Test
    public void compareSameVehicleReturnsZeroSavings() {
        ComparisonResult result = controller.compareVehicles(vehicle1, vehicle1);
        assertEquals(0, result.annualSavings());
    }

    @Test
    public void compareSameVehicleReturnsSameEfficiencyMessage() {
        ComparisonResult result = controller.compareVehicles(vehicle1, vehicle1);
        assertTrue(result.moreEfficientVehicle().contains("same efficiency"));
    }
}