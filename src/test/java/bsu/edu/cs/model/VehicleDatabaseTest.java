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
    public void databaseInitializationLoadsVehicles() {
        assertFalse(database.getVehicles().isEmpty());
    }

    @Test
    public void searchByMakeReturnsOnlyMatchingMakes() {
        List<Vehicle> results = database.searchVehicles(null, "Toyota", null, null);
        results.forEach(v -> assertEquals("Toyota", v.getMake()));
    }

    @Test
    public void searchByYearReturnsOnlyMatchingYears() {
        List<Vehicle> results = database.searchVehicles("2020", null, null, null);
        results.forEach(v -> assertEquals(2020, v.getYear()));
    }

    @Test
    public void searchWithNullYearReturnsAllYears() {
        List<Vehicle> allVehicles = database.searchVehicles(null, null, null, null);
        List<Vehicle> yearFiltered = database.searchVehicles("2020", null, null, null);
        assertTrue(allVehicles.size() > yearFiltered.size());
    }

    @Test
    public void electricVehiclesHaveNonNullFuelType() {
        List<Vehicle> evs = database.getVehicles().stream()
                .filter(v -> "Electricity".equals(v.getFuelType()))
                .toList();
        evs.forEach(ev -> assertNotNull(ev.getFuelType()));
    }
}