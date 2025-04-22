package bsu.edu.cs.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComparisonResultTest {
    private final Vehicle vehicle1 = new Vehicle("Tesla", "Model 3", 150.0, 2023);
    private final Vehicle vehicle2 = new Vehicle("Toyota", "Camry", 35.0, 2023);

    private final ComparisonResult.CostBreakdown cost1 = new ComparisonResult.CostBreakdown(
            850.0,    // annual
            70.83,    // monthly
            16.35,     // weekly
            2.33,      // daily
            0.057,     // perMile
            800.00,    // maintenance
            1650.00    // total
    );

    private final ComparisonResult.CostBreakdown cost2 = new ComparisonResult.CostBreakdown(
            1200.0,   // annual
            100.0,    // monthly
            23.08,    // weekly
            3.29,     // daily
            0.080,    // perMile
            800.00,   // maintenance
            2000.00   // total
    );

    private final ComparisonResult result = new ComparisonResult(
            vehicle1,
            vehicle2,
            cost1,
            cost2,
            350.0,     // annualSavings
            12,        // yearsOwned
            4200.0,    // yearsSavings
            "Tesla Model 3" // moreEfficientVehicle
    );

    @Test
    void vehicle1FieldIsCorrect() {
        assertEquals(vehicle1, result.vehicle1());
    }

    @Test
    void vehicle2FieldIsCorrect() {
        assertEquals(vehicle2, result.vehicle2());
    }

    @Test
    void cost1FieldIsCorrect() {
        assertEquals(cost1, result.cost1());
    }

    @Test
    void cost2FieldIsCorrect() {
        assertEquals(cost2, result.cost2());
    }

    @Test
    void annualSavingsFieldIsCorrect() {
        assertEquals(350.0, result.annualSavings());
    }

    @Test
    void yearsOwnedFieldIsCorrect() {
        assertEquals(12, result.yearsOwned());
    }

    @Test
    void yearsSavingsFieldIsCorrect() {
        assertEquals(4200.0, result.yearsSavings());
    }

    @Test
    void moreEfficientVehicleFieldIsCorrect() {
        assertEquals("Tesla Model 3", result.moreEfficientVehicle());
    }

    @Test
    void cost1ValuesAreCorrect() {
        assertEquals(850.0, result.cost1().annual(), 0.001);
        assertEquals(70.83, result.cost1().monthly(), 0.01);
        assertEquals(16.35, result.cost1().weekly(), 0.01);
        assertEquals(2.33, result.cost1().daily(), 0.01);
        assertEquals(0.057, result.cost1().perMile(), 0.001);
        assertEquals(800.00, result.cost1().maintenance(), 0.001);
        assertEquals(1650.00, result.cost1().total(), 0.001);
    }

    @Test
    void cost2ValuesAreCorrect() {
        assertEquals(1200.0, result.cost2().annual(), 0.001);
        assertEquals(100.0, result.cost2().monthly(), 0.01);
        assertEquals(23.08, result.cost2().weekly(), 0.01);
        assertEquals(3.29, result.cost2().daily(), 0.01);
        assertEquals(0.080, result.cost2().perMile(), 0.001);
        assertEquals(800.00, result.cost2().maintenance(), 0.001);
        assertEquals(2000.00, result.cost2().total(), 0.001);
    }

    @Test
    void calculatedYearCostsAreCorrect() {
        // Verify year costs are calculated correctly from annual costs and years owned
        assertEquals(850.0 * 12, result.cost1().annual() * result.yearsOwned(), 0.001);
        assertEquals(1200.0 * 12, result.cost2().annual() * result.yearsOwned(), 0.001);
    }
}