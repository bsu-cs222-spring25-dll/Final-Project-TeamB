package bsu.edu.cs.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class FuelEconomyServicesTest {

    private FuelEconomyService service;

    @BeforeEach
    public void setUp() throws Exception {
        service = new FuelEconomyService("src/test/resources/test_vehicles.csv");
    }

    @Test
    public void getYearsReturnsCurrentYear() throws ExecutionException, InterruptedException {
        int currentYear = java.time.Year.now().getValue();
        assertTrue(service.getYears().get().contains(String.valueOf(currentYear)));
    }

    @Test
    public void getMakesForYearReturnsCorrectMake() throws ExecutionException, InterruptedException {
        assertTrue(service.getMakes("2020").get().contains("Toyota"));
    }

    @Test
    public void getModelsForMakeReturnsCorrectModel() throws ExecutionException, InterruptedException {
        assertTrue(service.getModels("2020", "Toyota").get().contains("Camry"));
    }

    @Test
    public void searchVehiclesReturnsMatchingVehicle() throws ExecutionException, InterruptedException {
        Vehicle vehicle = service.searchVehicles("2020", "Toyota", "Camry", "LE").get();
        assertEquals("Toyota", vehicle.getMake());
    }

    @Test
    public void searchVehiclesWithNoMatchReturnsDefaultVehicle() throws ExecutionException, InterruptedException {
        Vehicle vehicle = service.searchVehicles("9999", "Unknown", "Model", "Trim").get();
        assertEquals("Unknown", vehicle.getMake());
    }
}