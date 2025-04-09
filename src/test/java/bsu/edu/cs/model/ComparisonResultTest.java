package bsu.edu.cs.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComparisonResultTest {
    private final Vehicle vehicle1 = new Vehicle("Tesla", "Model 3", 150.0, 2023);
    private final Vehicle vehicle2 = new Vehicle("Toyota", "Camry", 35.0, 2023);
    private final ComparisonResult result = new ComparisonResult(
            vehicle1, vehicle2,
            850.0, 1200.0,
            10200.0, 14400.0,
            350.0,
            12,
            4200.0,
            "Tesla Model 3",
            70.83, 100.0,
            16.35, 23.08,
            2.33, 3.29,
            0.057, 0.080
    );

    @Test void vehicle1FieldIsCorrect() { assertEquals(vehicle1, result.vehicle1()); }
    @Test void vehicle2FieldIsCorrect() { assertEquals(vehicle2, result.vehicle2()); }
    @Test void annualCost1FieldIsCorrect() { assertEquals(850.0, result.annualCost1()); }
    @Test void annualCost2FieldIsCorrect() { assertEquals(1200.0, result.annualCost2()); }
    @Test void yearCost1FieldIsCorrect() { assertEquals(10200.0, result.yearCost1()); }
    @Test void yearCost2FieldIsCorrect() { assertEquals(14400.0, result.yearCost2()); }
    @Test void annualSavingsFieldIsCorrect() { assertEquals(350.0, result.annualSavings()); }
    @Test void yearsOwnedFieldIsCorrect() { assertEquals(12, result.yearsOwned()); }
    @Test void yearsSavingsFieldIsCorrect() { assertEquals(4200.0, result.yearsSavings()); }
    @Test void moreEfficientVehicleFieldIsCorrect() { assertEquals("Tesla Model 3", result.moreEfficientVehicle()); }
    @Test void monthCost1FieldIsCorrect() { assertEquals(70.83, result.monthCost1(), 0.01); }
    @Test void monthCost2FieldIsCorrect() { assertEquals(100.0, result.monthCost2(), 0.01); }
    @Test void weekCost1FieldIsCorrect() { assertEquals(16.35, result.weekCost1(), 0.01); }
    @Test void weekCost2FieldIsCorrect() { assertEquals(23.08, result.weekCost2(), 0.01); }
    @Test void dayCost1FieldIsCorrect() { assertEquals(2.33, result.dayCost1(), 0.01); }
    @Test void dayCost2FieldIsCorrect() { assertEquals(3.29, result.dayCost2(), 0.01); }
    @Test void perMileCost1FieldIsCorrect() { assertEquals(0.057, result.perMileCost1(), 0.001); }
    @Test void perMileCost2FieldIsCorrect() { assertEquals(0.080, result.perMileCost2(), 0.001); }
}