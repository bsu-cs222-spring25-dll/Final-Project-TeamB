package bsu.edu.cs.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class FuelEconomyServicesTest {

    private FuelEconomyService service;

    @BeforeEach
    public void setUp() throws IOException {
        service = new FuelEconomyService("src/test/resources/test_vehicles.csv");
    }

    @Test
    public void testGetYears() throws ExecutionException, InterruptedException {
        List<String> years = service.getYears().get();
        assertFalse(years.isEmpty());
        assertTrue(years.contains(String.valueOf(java.time.Year.now().getValue())));
        assertTrue(years.contains("1984"));
    }

    @Test
    public void testGetMakesForYear() throws ExecutionException, InterruptedException {
        List<String> makes = service.getMakes("2020").get();
        assertFalse(makes.isEmpty());
        assertTrue(makes.contains("Toyota"));
    }

    @Test
    public void testGetModelsForMake() throws ExecutionException, InterruptedException {
        List<String> models = service.getModels("2020", "Toyota").get();
        assertFalse(models.isEmpty());
        assertTrue(models.contains("Camry"));
    }

    @Test
    public void testGetTrimsForModel() throws ExecutionException, InterruptedException {
        List<String> trims = service.getTrims("2020", "Toyota", "Camry").get();
        assertFalse(trims.isEmpty());
    }

    @Test
    public void testSearchVehicles() throws ExecutionException, InterruptedException {
        Vehicle vehicle = service.searchVehicles("2020", "Toyota", "Camry", "LE").get();
        assertNotNull(vehicle);
        assertEquals("Toyota", vehicle.getMake());
        assertEquals("Camry", vehicle.getModel());
        assertEquals("LE", vehicle.getTrim());
        assertEquals(2020, vehicle.getYear());
    }

    @Test
    public void testSearchVehiclesNoMatchReturnsDefault() throws ExecutionException, InterruptedException {
        Vehicle vehicle = service.searchVehicles("9999", "Unknown", "Model", "Trim").get();
        assertNotNull(vehicle);
        assertEquals("Unknown", vehicle.getMake());
        assertEquals("Model", vehicle.getModel());
        assertEquals("Trim", vehicle.getTrim());
    }
}