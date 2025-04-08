package bsu.edu.cs.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComparisonResultTest {
    private final Vehicle vehicle1 = new Vehicle("Toyota", "Camry", 30.0, 2023);
    private final Vehicle vehicle2 = new Vehicle("Honda", "Accord", 28.0, 2023);
    private final ComparisonResult result = new ComparisonResult(
            vehicle1, vehicle2,
            1200.0, 1300.0,
            6000.0, 6500.0,
            100.0, 5,
            500.0,
            "Toyota Camry"
    );

    @Test
    public void vehicle1IsCorrect() {
        assertEquals(vehicle1, result.vehicle1());
    }

    @Test
    public void vehicle2IsCorrect() {
        assertEquals(vehicle2, result.vehicle2());
    }

    @Test
    public void annualCost1IsCorrect() {
        assertEquals(1200.0, result.annualCost1());
    }

    @Test
    public void annualCost2IsCorrect() {
        assertEquals(1300.0, result.annualCost2());
    }

    @Test
    public void yearCost1IsCorrect() {
        assertEquals(6000.0, result.yearCost1());
    }

    @Test
    public void yearCost2IsCorrect() {
        assertEquals(6500.0, result.yearCost2());
    }

    @Test
    public void annualSavingsIsCorrect() {
        assertEquals(100.0, result.annualSavings());
    }

    @Test
    public void yearsOwnedIsCorrect() {
        assertEquals(5, result.yearsOwned());
    }

    @Test
    public void yearsSavingsIsCorrect() {
        assertEquals(500.0, result.yearsSavings());
    }

    @Test
    public void moreEfficientVehicleIsCorrect() {
        assertEquals("Toyota Camry", result.moreEfficientVehicle());
    }

    @Test
    public void equalsReturnsTrueForSameValues() {
        ComparisonResult other = new ComparisonResult(
                vehicle1, vehicle2,
                1200.0, 1300.0,
                6000.0, 6500.0,
                100.0, 5,
                500.0,
                "Toyota Camry"
        );
        assertEquals(result, other);
    }

    @Test
    public void hashCodeIsSameForSameValues() {
        ComparisonResult other = new ComparisonResult(
                vehicle1, vehicle2,
                1200.0, 1300.0,
                6000.0, 6500.0,
                100.0, 5,
                500.0,
                "Toyota Camry"
        );
        assertEquals(result.hashCode(), other.hashCode());
    }

    @Test
    public void toStringContainsVehicleInfo() {
        assertTrue(result.toString().contains("Toyota Camry"));
    }
}