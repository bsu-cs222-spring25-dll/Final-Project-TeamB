package bsu.edu.cs.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleDatabaseTest {

    private VehicleDatabase database;

    @BeforeEach
    public void setUp() throws IOException {
        database = new VehicleDatabase("src/test/resources/test_vehicles.csv");
    }

    @Test
    public void testDatabaseInitialization() {
        List<Vehicle> vehicles = database.getVehicles();
        assertNotNull(vehicles);
        assertFalse(vehicles.isEmpty());
    }

    @Test
    public void testSearchVehiclesByMake() {
        List<Vehicle> results = database.searchVehicles(null, "Toyota", null, null);
        assertFalse(results.isEmpty());
        results.forEach(v -> assertEquals("Toyota", v.getMake()));
    }

    @Test
    public void testSearchVehiclesByYearAndModel() {
        List<Vehicle> results = database.searchVehicles("2020", null, "Camry", null);
        assertFalse(results.isEmpty());
        results.forEach(v -> {
            assertEquals(2020, v.getYear());
            assertEquals("Camry", v.getModel());
        });
    }

    @Test
    public void testSearchVehiclesWithTrim() {
        List<Vehicle> results = database.searchVehicles(null, "Honda", "Civic", "EX");
        results.forEach(v -> {
            assertEquals("Honda", v.getMake());
            assertEquals("Civic", v.getModel());
            assertEquals("EX", v.getTrim());
        });
    }

    @Test
    public void testElectricVehicleImport() {
        List<Vehicle> evs = database.getVehicles().stream()
                .filter(v -> "Electricity".equals(v.getFuelType()))
                .toList();

        assertFalse(evs.isEmpty());
        evs.forEach(ev -> assertEquals("Electricity", ev.getFuelType()));
    }
}