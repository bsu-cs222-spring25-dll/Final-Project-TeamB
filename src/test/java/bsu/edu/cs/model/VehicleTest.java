package bsu.edu.cs.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VehicleTest {

    @Test
    public void testVehicleConstructorAndGetters() {
        Vehicle vehicle = new Vehicle("Toyota", "Camry", "LE", 30.0, 28.0, 32.0, "2023");

        assertEquals("Toyota", vehicle.getMake());
        assertEquals("Camry", vehicle.getModel());
        assertEquals("LE", vehicle.getTrim());
        assertEquals(30.0, vehicle.getCombinedMpg(), 0.001);
        assertEquals(28.0, vehicle.getCityMpg(), 0.001);
        assertEquals(32.0, vehicle.getHighwayMpg(), 0.001);
        assertEquals(2023, vehicle.getYear());
        assertNull(vehicle.getFuelType());
    }

    @Test
    public void testElectricVehicleConstructor() {
        Vehicle ev = new Vehicle("Tesla", "Model 3", "Standard", 120.0, 110.0, 130.0,
                "2023", "Electricity", 120.0);

        assertEquals("Tesla", ev.getMake());
        assertEquals("Electricity", ev.getFuelType());
        assertEquals(120.0, ev.getCombinedMpge(), 0.001);
    }

    @Test
    public void testToString() {
        Vehicle vehicle = new Vehicle("Honda", "Civic", "EX", 35.0, 32.0, 38.0, "2022");
        String expected = "2022 Honda Civic EX";
        assertEquals(expected, vehicle.toString());
    }

    @Test
    public void testSimplifiedConstructor() {
        Vehicle vehicle = new Vehicle("Ford", "F-150", 22.0, 2021);

        assertEquals("Ford", vehicle.getMake());
        assertEquals("F-150", vehicle.getModel());
        assertEquals(22.0, vehicle.getCombinedMpg(), 0.001);
        assertEquals(19.8, vehicle.getCityMpg(), 0.001);
        assertEquals(24.2, vehicle.getHighwayMpg(), 0.001);
    }
}