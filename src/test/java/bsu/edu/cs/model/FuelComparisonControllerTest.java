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
    public void setUp() throws Exception {
        calculator = new FuelCalculator();
        FuelEconomyService fuelEconomyService = new FuelEconomyService("src/main/resources/vehicles.csv");
        controller = new FuelComparisonControllerImpl(calculator, fuelEconomyService);
    }

    @Test
    public void compareVehiclesReturnsNonNullResult() {
        ComparisonResult result = controller.compareVehicles(vehicle1, vehicle2);
        assertNotNull(result);
        assertNotNull(result.cost1());
        assertNotNull(result.cost2());
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
    public void compareVehiclesReturnsPositiveCosts() {
        ComparisonResult result = controller.compareVehicles(vehicle1, vehicle2);
        assertTrue(result.cost1().annual() > 0);
        assertTrue(result.cost2().annual() > 0);
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
        assertEquals("Custom Vehicle", vehicle.getMake());
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

    @Test
    public void updateSettingsFromStringsWithValidInputUpdatesCalculator() {
        controller.updateSettingsFromStrings("3.50", "15000", "5", "0.13");
        assertEquals(3.50, calculator.getAnnualGasPrice());
        assertEquals(15000, calculator.getAnnualMiles());
        assertEquals(5, calculator.getYearsOwned());
        assertEquals(0.13, calculator.getElectricityPricePerKWH());
    }

    @Test
    public void updateSettingsFromStringsWithInvalidInputThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                controller.updateSettingsFromStrings("invalid", "15000", "5", "0.13"));
    }

    @Test
    public void createVehicleFromInputsWithMPGReturnsCustomVehicle() {
        Vehicle custom = controller.createVehicleFromInputs("35.0", "Custom Car", null);
        assertEquals("Custom Vehicle", custom.getMake());
        assertEquals("Custom Car", custom.getModel());
        assertEquals(35.0, custom.getCombinedMpg());
    }

    @Test
    public void createVehicleFromInputsWithSelectedVehicleReturnsSelected() {
        Vehicle selected = controller.createVehicleFromInputs(null, null, vehicle1);
        assertEquals(vehicle1, selected);
    }

    @Test
    public void createVehicleFromInputsWithNoInputThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                controller.createVehicleFromInputs(null, null, null));
    }

    @Test
    public void updateFinancialSettingsUpdatesVehicle1Correctly() {
        controller.setCurrentVehicles(vehicle1, vehicle2);
        controller.updateFinancialSettings(
                25000, 5000, 20000, 5.0, 60,
                0, 0, 0, 0, 0
        );
        assertEquals(25000, vehicle1.getPurchasePrice());
        assertEquals(5000, vehicle1.getDownPayment());
        assertEquals(20000, vehicle1.getLoanAmount());
        assertEquals(5.0, vehicle1.getInterestRate());
        assertEquals(60, vehicle1.getLoanPeriod());
    }

    @Test
    public void updateFinancialSettingsUpdatesVehicle2Correctly() {
        controller.setCurrentVehicles(vehicle1, vehicle2);
        controller.updateFinancialSettings(
                0, 0, 0, 0, 0,
                30000, 6000, 24000, 4.5, 72
        );
        assertEquals(30000, vehicle2.getPurchasePrice());
        assertEquals(6000, vehicle2.getDownPayment());
        assertEquals(24000, vehicle2.getLoanAmount());
        assertEquals(4.5, vehicle2.getInterestRate());
        assertEquals(72, vehicle2.getLoanPeriod());
    }

    @Test
    public void compareVehiclesReturnsValidCostBreakdowns() {
        ComparisonResult result = controller.compareVehicles(vehicle1, vehicle2);

        // Test cost1 breakdown
        assertTrue(result.cost1().annual() > 0);
        assertTrue(result.cost1().monthly() > 0);
        assertTrue(result.cost1().weekly() > 0);
        assertTrue(result.cost1().daily() > 0);
        assertTrue(result.cost1().perMile() > 0);
        assertTrue(result.cost1().maintenance() > 0);
        assertTrue(result.cost1().total() > 0);

        // Test cost2 breakdown
        assertTrue(result.cost2().annual() > 0);
        assertTrue(result.cost2().monthly() > 0);
        assertTrue(result.cost2().weekly() > 0);
        assertTrue(result.cost2().daily() > 0);
        assertTrue(result.cost2().perMile() > 0);
        assertTrue(result.cost2().maintenance() > 0);
        assertTrue(result.cost2().total() > 0);
    }
}